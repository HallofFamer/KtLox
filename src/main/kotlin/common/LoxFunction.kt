package com.mysidia.ktlox.common

import com.mysidia.ktlox.ast.Expr
import com.mysidia.ktlox.interpreter.Environment
import com.mysidia.ktlox.interpreter.Interpreter
import com.mysidia.ktlox.interpreter.Return
import com.mysidia.ktlox.std.lang.FunctionClass

class LoxFunction(val name: String?,
                  private val declaration: Expr.Function,
                  private val closure: Environment,
                  private val isInitializer: Boolean) : LoxObject(FunctionClass), LoxCallable{

    override val arity = if(declaration.params != null) declaration.params.size else 0
    override val isGetter = declaration.params == null

    override fun bind(instance: LoxObject): LoxCallable{
        val environment = Environment(closure)
        environment.define("this", instance)
        return LoxFunction(name, declaration, environment, isInitializer)
    }

    override fun call(interpreter: Interpreter, arguments: List<Any?>?): Any?{
        val environment = Environment(closure)
        declaration.params?.forEachIndexed { index, token -> environment.define(token.lexeme, arguments!![index]) }
        try{
            interpreter.executeBlock(declaration.body, environment)
        }
        catch(returnValue: Return){
            return if(isInitializer) closure.getAt(0, "this") else returnValue.value
        }
        return if(isInitializer) closure.getAt(0, "this") else null
    }

    override fun toString() = if(name == null) "<fn>" else "<fn ${name}>"
}