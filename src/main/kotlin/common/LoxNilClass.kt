package com.mysidia.ktlox.common

import com.mysidia.ktlox.interpreter.Interpreter
import com.mysidia.ktlox.interpreter.RuntimeError

object LoxNilClass : LoxNativeClass("Nil", LoxObjectClass){

    init{
        defineNativeMetaclass("Nil class")
        defineNativeMethod("init", 0, this::initDef)
        defineNativeMethod("isNil", 0, this::isNilDef)
        defineNativeMethod("isNilOrEmpty", 0, this::isNilOrEmptyDef)
        defineNativeMethod("toString", 0, this::toStringDef)
    }

    private fun initDef(interpreter: Interpreter, arguments: List<Any?>?) : Any?{
        throw RuntimeError(interpreter.tokenNil, "Cannot create instance from class Nil")
    }

    private fun isNilDef(interpreter: Interpreter, arguments: List<Any?>?) = true

    private fun isNilOrEmptyDef(interpreter: Interpreter, arguments: List<Any?>?) = true

    private fun toStringDef(interpreter: Interpreter, arguments: List<Any?>?) = "nil"
}