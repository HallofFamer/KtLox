package com.mysidia.ktlox.std.lang

import com.mysidia.ktlox.common.*
import com.mysidia.ktlox.interpreter.ArgumentError
import com.mysidia.ktlox.interpreter.Interpreter

object BooleanClass : LoxNativeClass("Boolean", ObjectClass){

    init{
        defineNativeMetaclass("Boolean class")
        defineNativeMethod("init", 0, this::initDef)
        defineNativeMethod("isBoolean", 0, this::isBooleanDef)
    }

    override fun initDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxObject{
        throw ArgumentError("Cannot create instance from abstract class Boolean.")
    }

    private fun isBooleanDef(interpreter: Interpreter, arguments: List<Any?>?) = true
}