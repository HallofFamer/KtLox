package com.mysidia.ktlox.std.lang

import com.mysidia.ktlox.common.*
import com.mysidia.ktlox.interpreter.Interpreter

object FunctionClass : LoxNativeClass("Function", ObjectClass) {

    init{
        defineNativeMetaclass("Function class")
        defineNativeMethod("arity", 0, this::arityDef)
    }

    private fun arityDef(interpreter: Interpreter, arguments: List<Any?>?) = (interpreter.thisInstance as LoxCallable).arity
}