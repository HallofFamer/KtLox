package com.mysidia.ktlox.common

import com.mysidia.ktlox.interpreter.Interpreter
import com.mysidia.ktlox.std.lang.FunctionClass

abstract class LoxNativeFunction(protected val name: String,
                                 override val arity: Int,
                                 protected val body: (arguments: List<Any?>?) -> Any?
) : LoxObject(FunctionClass), LoxCallable {

    override val isGetter = false
    override fun call(interpreter: Interpreter, arguments: List<Any?>?) = body.invoke(arguments)
    override fun toString() = "<native fn: $name>"
}