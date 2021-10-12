package com.mysidia.ktlox.common

import com.mysidia.ktlox.interpreter.Environment
import com.mysidia.ktlox.interpreter.Interpreter

open class LoxNativeMethod(val name: String,
                           override val arity: Int,
                           override val isGetter: Boolean,
                           private val closure: Environment?,
                           private val body: (interpreter : Interpreter, arguments: List<Any?>?) -> Any?) : LoxObject(LoxFunctionClass), LoxCallable{

    override fun bind(instance: LoxObject): LoxNativeMethod {
        val environment = Environment(closure)
        environment.define("this", instance)
        return LoxNativeMethod(name, arity, isGetter, environment, body)
    }

    override fun call(interpreter: Interpreter, arguments: List<Any?>?): Any? {
        val enclosing = interpreter.environment
        interpreter.environment = closure!!
        val result = body.invoke(interpreter, arguments)
        interpreter.environment = enclosing
        return result
    }
}