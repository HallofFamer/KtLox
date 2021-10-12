package com.mysidia.ktlox.common

import com.mysidia.ktlox.interpreter.Interpreter

object LoxBooleanClass : LoxNativeClass("Boolean", LoxObjectClass){

    init{
        defineNativeMetaclass("Boolean class")
        defineNativeMethod("isBoolean", 0, this::isBooleanDef)
    }

    private fun isBooleanDef(interpreter: Interpreter, arguments: List<Any?>?) = true
}