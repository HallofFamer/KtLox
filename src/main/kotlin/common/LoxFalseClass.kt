package com.mysidia.ktlox.common

import com.mysidia.ktlox.interpreter.Interpreter
import com.mysidia.ktlox.interpreter.RuntimeError

object LoxFalseClass : LoxNativeClass("False", LoxBooleanClass) {

    init{
        defineNativeMetaclass("False class")
        defineNativeMethod("init", 0, this::initDef)
        defineNativeMethod("toString", 0, this::toStringDef)
    }

    private fun initDef(interpreter: Interpreter, arguments: List<Any?>?) : Any?{
        throw RuntimeError(interpreter.tokenFalse, "Cannot create instance from class False")
    }

    private fun toStringDef(interpreter: Interpreter, arguments: List<Any?>?) = "false"
}