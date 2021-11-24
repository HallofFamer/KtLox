package com.mysidia.ktlox.interpreter

import com.mysidia.ktlox.Lox
import com.mysidia.ktlox.ast.*
import com.mysidia.ktlox.common.*
import com.mysidia.ktlox.lexer.Token
import com.mysidia.ktlox.lexer.TokenType.*
import com.mysidia.ktlox.std.lang.*
import java.io.FileInputStream
import java.util.*

class Interpreter(private val configs: Properties) : Expr.Visitor<Any?>, Stmt.Visitor<Unit>{

    private val globals = Environment()
    private val locals = mutableMapOf<Expr, Int>()
    var environment = globals

    val thisInstance: LoxObject get() = environment.getAt(0, "this") as? LoxObject ?: LoxNil
    val tokenFalse = Token(FALSE, "false", false, 0)
    val tokenNil = Token(NIL, "nil", null, 0)
    val tokenTrue = Token(TRUE, "true", true, 0)

    init{
        configs.load(FileInputStream("./config.properties"))
        resolveModules()
    }

    fun executeBlock(statements: List<Stmt>, environment: Environment) {
        val previous = this.environment
        try{
            this.environment = environment
            statements.forEach(::execute)
        }
        finally{
            this.environment = previous
        }
    }

    fun interpret(stmts: List<Stmt>){
        try{
            stmts.forEach(::execute)
        }
        catch(error: RuntimeError){
            Lox.runtimeError(error)
        }
    }

    fun resolve(expr: Expr, depth: Int) {
        locals[expr] = depth
    }
    
    override fun visitAssignExpr(expr: Expr.Assign): Any? {
        val value = evaluate(expr.value)
        val distance = locals[expr]
        if(distance != null) environment.assignAt(distance, expr.name, value) else globals.assign(expr.name, value)
        return value
    }

    override fun visitBinaryExpr(expr: Expr.Binary): Any {
        val left = evaluate(expr.left)
        val right = evaluate(expr.right)
        return when (expr.operator.type) {
            BANG_EQUAL -> !isEqual(left, right)
            EQUAL_EQUAL -> isEqual(left, right)
            GREATER -> evaluateGreater(expr.operator, left, right)
            GREATER_EQUAL -> evaluateGreaterEqual(expr.operator, left, right)
            LESS -> evaluateLess(expr.operator, left, right)
            LESS_EQUAL -> evaluateLessEqual(expr.operator, left, right)
            PLUS -> evaluatePlus(expr.operator, left, right)
            MINUS -> evaluateMinus(expr.operator, left, right)
            STAR -> evaluateStar(expr.operator, left, right)
            SLASH -> evaluateSlash(expr.operator, left, right)
            MODULUS -> evaluateModulus(expr.operator, left, right)
            else -> throw RuntimeException("Unidentified binary expression identified!")
        }
    }

    override fun visitBlockStmt(stmt: Stmt.Block) = executeBlock(stmt.statements, Environment(environment))

    override fun visitBreakStmt(stmt: Stmt.Break) = throw BreakException()

    override fun visitCallExpr(expr: Expr.Call): Any? {
        try {
            val callee = evaluate(expr.callee)
            val arguments = expr.arguments!!.map { evaluate(it) }
            if(callee !is LoxCallable) throw RuntimeError(expr.paren, "can only call functions and classes.")
            if(arguments.size != callee.arity) throw RuntimeError(expr.paren, "Expected ${callee.arity} arguments but got ${arguments.size} instead.")
            return callee.call(this, arguments)
        }
        catch(ex: ArgumentError){
            throw RuntimeError(expr.paren, ex.message!!)
        }
    }

    override fun visitClassStmt(stmt: Stmt.Class) {
        environment.define(stmt.name.lexeme, null)
        val superclass = evaluate(stmt.superclass)
        if(superclass !is LoxClass) throw RuntimeError(stmt.superclass.name, "Superclass must be a class.")
        environment = Environment(environment)
        environment.define("super", superclass)

        val classMethods = mutableMapOf<String, LoxCallable>()
        var classInitializer : LoxFunction? = null
        stmt.classMethods.forEach {
            val method = if(it.name.lexeme == "init"){
                classInitializer = LoxFunction(it.name.lexeme, it.functionBody, environment, true)
                classInitializer!!
            } else LoxFunction(it.name.lexeme, it.functionBody, environment, false)
            classMethods[it.name.lexeme] = method
        }

        val metaclass = LoxClass("${stmt.name.lexeme} class", ClassClass, classMethods, null, ClassClass)
        val traits = evaluateTraits(stmt.traits)
        val methods = applyTraits(stmt.traits, traits)
        stmt.methods.forEach {
            val method = LoxFunction(it.name.lexeme, it.functionBody, environment, it.name.lexeme == "init")
            methods[it.name.lexeme] = method
        }
        val klass = LoxClass(stmt.name.lexeme, superclass as LoxClass?, methods, traits.toMutableList(), metaclass)
        classInitializer?.bind(klass)?.call(this, listOf())
        environment = environment.enclosing!!
        environment.assign(stmt.name, klass)
    }

    override fun visitExpressionStmt(stmt: Stmt.Expression){
        evaluate(stmt.expression)
    }

    override fun visitFunctionExpr(expr: Expr.Function): Any {
        return LoxFunction(null, expr, environment, false)
    }

    override fun visitFunctionStmt(stmt: Stmt.Function) {
        val function = LoxFunction(stmt.name.lexeme, stmt.functionBody, environment, false)
        environment.define(stmt.name.lexeme, function)
    }

    override fun visitGetExpr(expr: Expr.Get): Any? {
        val result = when(val obj = evaluate(expr.obj)){
            is LoxObject -> obj.get(expr.name)
            null -> LoxNil.get(expr.name)
            true -> LoxTrue.get(expr.name)
            false -> LoxFalse.get(expr.name)
            is Long -> LoxInt.reset(obj).get(expr.name)
            is Double -> LoxFloat.reset(obj).get(expr.name)
            is String -> LoxString.reset(obj).get(expr.name)
            else -> throw RuntimeError(expr.name, "Accessing property on unidentified token: ")
        }
        if(result is LoxCallable && result.isGetter) return result.call(this, null)
        return result
    }

    override fun visitGroupingExpr(expr: Expr.Grouping) = evaluate(expr.expression)

    override fun visitIfStmt(stmt: Stmt.If) {
        if(isTruthy(evaluate(stmt.condition))) execute(stmt.thenBranch)
        else execute(stmt.elseBranch ?: return)
    }

    override fun visitLiteralExpr(expr: Expr.Literal) = expr.value

    override fun visitLogicalExpr(expr: Expr.Logical): Any? {
        val left = evaluate(expr.left)
        return if((expr.operator.type == OR && isTruthy(left)) || !isTruthy(left)) left
               else evaluate(expr.right)
    }

    override fun visitReturnStmt(stmt: Stmt.Return) {
        val value = if(stmt.value != null) evaluate(stmt.value) else null
        throw Return(value)
    }

    override fun visitSetExpr(expr: Expr.Set): Any? {
        val obj = evaluate(expr.obj) as? LoxObject ?: throw RuntimeError(expr.name, "Only instances have fields.")
        val value = evaluate(expr.value)
        obj.set(expr.name, value)
        return value
    }

    override fun visitSuperExpr(expr: Expr.Super): LoxCallable {
        val distance = locals[expr]!!
        val superClass = environment.getAt(distance, "super") as LoxClass
        val thisInstance = environment.getAt(distance - 1, "this") as LoxObject
        val method = superClass.findMethod(expr.method.lexeme)
            ?: throw RuntimeError(expr.method, "Undefined method '${expr.method.lexeme}'.")
        return method.bind(thisInstance)
    }

    override fun visitThisExpr(expr: Expr.This): Any? {
        return lookUpVariable(expr.keyword, expr)
    }

    override fun visitTraitStmt(stmt: Stmt.Trait) {
        environment.define(stmt.name.lexeme, null)
        val traits = evaluateTraits(stmt.traits)
        val methods = applyTraits(stmt.traits, traits)
        stmt.methods.forEach {
            methods[it.name.lexeme] = LoxFunction(it.name.lexeme, it.functionBody, environment, false)
        }
        val trait = LoxTrait(stmt.name.lexeme, methods, traits)
        environment.assign(stmt.name, trait)
    }

    override fun visitUnaryExpr(expr: Expr.Unary): Any? {
        val right = evaluate(expr.right)
        return when(expr.operator.type){
            MINUS -> evaluateNegate(expr.operator, right)
            BANG -> !isTruthy(right)
            else -> null
        }
    }

    override fun visitVariableExpr(expr: Expr.Variable) : Any?{
        return lookUpVariable(expr.name, expr)
    }

    override fun visitVarStmt(stmt: Stmt.Var) {
        val value = if(stmt.initializer != null) evaluate(stmt.initializer) else Environment.Unassigned
        environment.define(stmt.name.lexeme, value)
    }

    override fun visitWhileStmt(stmt: Stmt.While) {
        try{
            while(isTruthy(evaluate(stmt.condition))){
                execute(stmt.body)
            }
        }
        catch(ex: BreakException){ }
    }


    private fun applyTraits(traits: List<Expr.Variable>, traitObjects: List<LoxTrait>) : MutableMap<String, LoxCallable>{
        val methods = mutableMapOf<String, LoxCallable>()
        traits.forEachIndexed { index, variable ->
            val trait = traitObjects[index]
            trait.methods.forEach { (name, method) ->
                if(methods.containsKey(name)) throw RuntimeError(variable.name, "Conflicting method $name found.")
                methods[name] = method
            }
        }
        return methods
    }

    private fun evaluate(expr: Expr) = expr.accept(this)

    private fun evaluateGreater(operator: Token, left: Any?, right: Any?) : Boolean{
        if(left is Long && right is Long) return left > right
        if(left is Number && right is Number) return left.toDouble() > right.toDouble()
        throw RuntimeError(operator, "Operands for greater operator must be two numbers.")
    }

    private fun evaluateGreaterEqual(operator: Token, left: Any?, right: Any?) : Boolean{
        if(left is Long && right is Long) return left >= right
        if(left is Number && right is Number) return left.toDouble() >= right.toDouble()
        throw RuntimeError(operator, "Operands for greater_equal operator must be two numbers.")
    }

    private fun evaluateLess(operator: Token, left: Any?, right: Any?) : Boolean{
        if(left is Long && right is Long) return left < right
        if(left is Number && right is Number) return left.toDouble() < right.toDouble()
        throw RuntimeError(operator, "Operands for greater operator must be two numbers.")
    }

    private fun evaluateLessEqual(operator: Token, left: Any?, right: Any?) : Boolean{
        if(left is Long && right is Long) return left <= right
        if(left is Number && right is Number) return left.toDouble() <= right.toDouble()
        throw RuntimeError(operator, "Operands for less_equal operator must be two numbers.")
    }

    private fun evaluateMinus(operator: Token, left: Any?, right: Any?): Any {
        if(left is Long && right is Long) return left - right
        if(left is Number && right is Number) return left.toDouble() - right.toDouble()
        throw RuntimeError(operator, "Operands for minus operator must be two numbers.")
    }

    private fun evaluateModulus(operator: Token, left: Any?, right: Any?): Any{
        if(left is Long && right is Long) return left % right
        if(left is Number && right is Number) return left.toDouble() % right.toDouble()
        throw RuntimeError(operator, "Operands for modulus operator must be two numbers.")
    }

    private fun evaluateNegate(operator: Token, operand: Any?) : Number{
        return when(operand){
            is Long -> -operand
            is Double -> -operand
            else -> throw RuntimeError(operator, "Operand must be a number.")
        }
    }

    private fun evaluatePlus(operator: Token, left: Any?, right: Any?): Any {
        if(left is Long && right is Long) return left + right
        if(left is Number && right is Number) return left.toDouble() + right.toDouble()
        if(left is String && right is String) return stringify(left) + stringify(right)
        throw RuntimeError(operator, "Operands for plus operator must be two numbers or two strings.")
    }

    private fun evaluateSlash(operator: Token, left: Any?, right: Any?) : Any {
        if(right == 0L || right == 0.0) throw RuntimeError(operator, "Cannot divide a number by 0!")
        if(left is Long && right is Long) return left / right
        if(left is Number && right is Number) return left.toDouble() / right.toDouble()
        throw RuntimeError(operator, "Operands for divide operator must be two numbers.")
    }

    private fun evaluateStar(operator: Token, left: Any?, right: Any?) : Any {
        if(left is Long && right is Long) return left * right
        if(left is Number && right is Number) return left.toDouble() * right.toDouble()
        throw RuntimeError(operator, "Operands for times operator must be two numbers.")
    }

    private fun evaluateTraits(traits: List<Expr.Variable>) = traits.map {
            val trait = evaluate(it)
            if(trait !is LoxTrait) throw RuntimeError(it.name, "'${it.name.lexeme}' is not a trait.")
            trait
        }

    private fun execute(stmt: Stmt) = stmt.accept(this)

    private fun isEqual(left: Any?, right: Any?) = (left == null && right == null) || (left?.equals(right) ?: false)

    private fun isTruthy(obj: Any?) = if (obj is Boolean) obj else (obj != null)

    private fun lookUpVariable(name: Token, expr: Expr): Any? {
        val distance = locals[expr]
        return if(distance != null){ environment.getAt(distance, name.lexeme) } else globals.get(name)
    }

    private fun resolveModules(){
        val stdPackage = configs.getProperty("std.package")
        val modules = configs.getProperty("std.modules")
        modules.split(",").forEach {
            val moduleName = "$stdPackage.$it.${it.capitalize()}Module"
            val module = Class.forName(moduleName).kotlin.objectInstance as LoxModule
            module.registerModule(globals)
        }
    }

    private fun stringify(obj: Any?): String {
        if(obj == null) return "nil"
        val text = obj.toString()
        return if(obj is Double && text.endsWith(".0")) text.substring(0, text.length - 2) else text
    }
}