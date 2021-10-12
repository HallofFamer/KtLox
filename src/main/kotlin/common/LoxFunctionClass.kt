package com.mysidia.ktlox.common

import com.mysidia.ktlox.interpreter.Interpreter

object LoxFunctionClass : LoxNativeClass("Function", LoxObjectClass) {

    init{
        defineNativeMetaclass("Function class")
        defineNativeMethod("arity", 0, this::arityDef)
    }

    private fun arityDef(interpreter: Interpreter, arguments: List<Any?>?) = (interpreter.thisInstance as LoxCallable).arity
}