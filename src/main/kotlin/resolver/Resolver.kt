package com.mysidia.ktlox.resolver

import com.mysidia.ktlox.Lox
import com.mysidia.ktlox.ast.Expr
import com.mysidia.ktlox.ast.Stmt
import com.mysidia.ktlox.interpreter.Interpreter
import com.mysidia.ktlox.lexer.Token
import com.mysidia.ktlox.lexer.TokenType
import java.util.*

class Resolver(private val interpreter: Interpreter) : Expr.Visitor<Unit>, Stmt.Visitor<Unit>{

    private val scopes = Stack<MutableMap<String, Variable>>()
    private var currentClass = ClassType.NONE
    private var currentFunction = FunctionType.NONE
    private val implicitSuper = Token(TokenType.SUPER, "super", null, 0)
    private val implicitThis = Token(TokenType.THIS, "this", null, 0)

    fun resolve(statements: List<Stmt>) = statements.forEach{ resolve(it) }

    override fun visitAssignExpr(expr: Expr.Assign) {
        resolve(expr.value)
        resolveLocal(expr, expr.name, false)
    }

    override fun visitBinaryExpr(expr: Expr.Binary) {
        resolve(expr.left)
        resolve(expr.right)
    }

    override fun visitCallExpr(expr: Expr.Call) {
        resolve(expr.callee)
        expr.arguments!!.forEach(::resolve)
    }

    override fun visitFunctionExpr(expr: Expr.Function) {
        resolveFunction(expr, FunctionType.FUNCTION)
    }

    override fun visitGetExpr(expr: Expr.Get) {
        resolve(expr.obj)
    }

    override fun visitGroupingExpr(expr: Expr.Grouping) {
        resolve(expr.expression)
    }

    override fun visitLiteralExpr(expr: Expr.Literal) {
        return
    }

    override fun visitLogicalExpr(expr: Expr.Logical) {
        resolve(expr.left)
        resolve(expr.right)
    }

    override fun visitSetExpr(expr: Expr.Set) {
        resolve(expr.value)
        resolve(expr.obj)
    }

    override fun visitSuperExpr(expr: Expr.Super) {
        when(currentClass){
            ClassType.NONE -> Lox.error(expr.keyword, "Cannot use 'super' outside of a class.")
            ClassType.CLASS -> Lox.error(expr.keyword, "Cannot use 'super' without superclass.")
            ClassType.TRAIT -> Lox.error(expr.keyword, "Cannot use 'super' within a trait.")
        }
        resolveLocal(expr, expr.keyword, true)
    }

    override fun visitThisExpr(expr: Expr.This) {
        if(currentClass == ClassType.NONE){
            Lox.error(expr.keyword, "Cannot use 'this' outside of a class.")
        }
        resolveLocal(expr, expr.keyword, true)
    }

    override fun visitUnaryExpr(expr: Expr.Unary) {
        resolve(expr.right)
    }

    override fun visitVariableExpr(expr: Expr.Variable) {
        if(!scopes.isEmpty() &&
            scopes.peek().containsKey(expr.name.lexeme) &&
            scopes.peek()[expr.name.lexeme]!!.state == VariableState.DECLARED){
            Lox.error(expr.name, "Cannot read local variables in its own initializer.")
        }
        resolveLocal(expr, expr.name, true)
    }

    override fun visitBlockStmt(stmt: Stmt.Block) {
        beginScope()
        resolve(stmt.statements)
        endScope()
    }

    override fun visitBreakStmt(stmt: Stmt.Break) {
        return
    }

    override fun visitClassStmt(stmt: Stmt.Class) {
        val enclosingClass = currentClass
        currentClass = ClassType.CLASS
        declare(stmt.name)
        define(stmt.name)
        stmt.superclass?.let {
            if(stmt.name.lexeme == it.name.lexeme){
                Lox.error(it.name, "A class cannot inherit from itself.")
            }
            currentClass = ClassType.SUBCLASS
            resolve(it)
            beginScope()
            scopes.peek()["super"] = Variable(implicitSuper, VariableState.READ)
        }
        stmt.traits.forEach { resolve(it) }

        beginScope()
        scopes.peek()["this"] = Variable(implicitThis, VariableState.READ)
        stmt.methods.forEach{
            val declaration = if(it.name.lexeme == "init") FunctionType.INITIALIZER else FunctionType.METHOD
            resolveFunction(it.functionBody, declaration)
        }
        stmt.classMethods.forEach {
            beginScope()
            scopes.peek()["this"] = Variable(implicitThis, VariableState.READ)
            val declaration = if(it.name.lexeme == "init"){
                if(it.functionBody.params!!.isNotEmpty()) Lox.error(it.name, "class method ${it.name.lexeme} cannot have parameters.")
                FunctionType.INITIALIZER
            }
            else FunctionType.METHOD
            resolveFunction(it.functionBody, declaration)
            endScope()
        }
        endScope()
        stmt.superclass?.let { endScope() }
        currentClass = enclosingClass
    }

    override fun visitFunctionStmt(stmt: Stmt.Function) {
        declare(stmt.name)
        define(stmt.name)
        resolveFunction(stmt.functionBody, FunctionType.FUNCTION)
    }

    override fun visitExpressionStmt(stmt: Stmt.Expression) {
        resolve(stmt.expression)
    }

    override fun visitIfStmt(stmt: Stmt.If) {
        resolve(stmt.condition)
        resolve(stmt.thenBranch)
        stmt.elseBranch?.let { resolve(it) }
    }

    override fun visitPrintStmt(stmt: Stmt.Print) {
        resolve(stmt.expression)
    }

    override fun visitReturnStmt(stmt: Stmt.Return) {
        if(currentFunction == FunctionType.NONE){
            Lox.error(stmt.keyword, "Cannot return from top-level code.")
        }
        stmt.value?.let {
            if(currentFunction == FunctionType.INITIALIZER){
                Lox.error(stmt.keyword, "Cannot return value from a class initializer.")
            }
            resolve(it)
        }
    }

    override fun visitTraitStmt(stmt: Stmt.Trait) {
        val enclosingClass = currentClass
        currentClass = ClassType.TRAIT
        declare(stmt.name)
        define(stmt.name)
        stmt.traits.forEach {
            if(it.name.lexeme == stmt.name.lexeme) Lox.error(stmt.name, "A trait cannot compose itself.")
            resolve(it)
        }

        beginScope()
        scopes.peek()["this"] = Variable(implicitThis, VariableState.READ)
        stmt.methods.forEach {
            if(it.name.lexeme == "init") Lox.error(stmt.name, "A trait cannot have an initializer method.")
            resolveFunction(it.functionBody, FunctionType.METHOD)
        }
        endScope()
        currentClass = enclosingClass
    }

    override fun visitVarStmt(stmt: Stmt.Var) {
        declare(stmt.name)
        stmt.initializer?.let { resolve(it) }
        define(stmt.name)
    }

    override fun visitWhileStmt(stmt: Stmt.While) {
        resolve(stmt.condition)
        resolve(stmt.body)
    }


    private fun beginScope() = scopes.push(mutableMapOf())

    private fun declare(name: Token) {
        if(scopes.isEmpty()) return
        val scope = scopes.peek()
        if(scope.containsKey(name.lexeme)){
            Lox.error(name, "Already has variable with the same name in this scope.")
        }
        scope[name.lexeme] = Variable(name, VariableState.DECLARED)
    }

    private fun define(name: Token) {
        if(scopes.isEmpty()) return
        scopes.peek()[name.lexeme]!!.state = VariableState.DEFINED
    }

    private fun endScope(){
        val scope = scopes.pop()
        scope.forEach { (_, value) ->
            if(value.state == VariableState.DEFINED){
                Lox.error(value.name, "Local variable is not used.")
            }
        }
    }

    private fun resolve(expr: Expr) = expr.accept(this)

    private fun resolve(stmt: Stmt) = stmt.accept(this)

    private fun resolveFunction(expr: Expr.Function, type: FunctionType) {
        val enclosingFunction = currentFunction
        currentFunction = type
        beginScope()
        expr.params?.forEach {
            declare(it)
            define(it)
        }
        resolve(expr.body)
        endScope()
        currentFunction = enclosingFunction
    }

    private fun resolveLocal(expr: Expr, name: Token, isRead: Boolean) {
        scopes.asReversed().toMutableList().forEachIndexed { index, scope ->
            if(scope.containsKey(name.lexeme)){
                val variable = scope[name.lexeme]!!
                interpreter.resolve(expr, index)
                if(isRead) variable.state = VariableState.READ
                return
            }
        }
    }
}