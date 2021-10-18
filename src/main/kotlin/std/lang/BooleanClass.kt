package com.mysidia.ktlox.std.lang

import com.mysidia.ktlox.common.LoxNativeClass
import com.mysidia.ktlox.interpreter.Interpreter

object BooleanClass : LoxNativeClass("Boolean", ObjectClass){

    init{
        defineNativeMetaclass("Boolean class")
        defineNativeMethod("isBoolean", 0, this::isBooleanDef)
    }

    private fun isBooleanDef(interpreter: Interpreter, arguments: List<Any?>?) = true
}