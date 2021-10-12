package com.mysidia.ktlox.common

import com.mysidia.ktlox.interpreter.Interpreter

abstract class LoxNativeFunction(protected val name: String,
                                 override val arity: Int,
                                 protected val body: () -> Any?
) : LoxObject(LoxFunctionClass), LoxCallable {

    override val isGetter = false
    override fun call(interpreter: Interpreter, arguments: List<Any?>?) = body.invoke()
    override fun toString() = "<native fn: $name>"
}