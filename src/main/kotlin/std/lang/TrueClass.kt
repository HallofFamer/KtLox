package com.mysidia.ktlox.std.lang

import com.mysidia.ktlox.common.LoxNativeClass
import com.mysidia.ktlox.interpreter.Interpreter
import com.mysidia.ktlox.interpreter.RuntimeError

object TrueClass : LoxNativeClass("True", BooleanClass) {

    init{
        defineNativeMetaclass("True class")
        defineNativeMethod("init", 0, this::initDef)
        defineNativeMethod("toString", 0, this::toStringDef)
    }

    private fun initDef(interpreter: Interpreter, arguments: List<Any?>?) : Any?{
        throw RuntimeError(interpreter.tokenTrue, "Cannot create instance from class True")
    }

    private fun toStringDef(interpreter: Interpreter, arguments: List<Any?>?) = "true"
}