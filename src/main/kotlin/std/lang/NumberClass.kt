package com.mysidia.ktlox.std.lang

import com.mysidia.ktlox.common.*
import com.mysidia.ktlox.interpreter.Interpreter

object NumberClass : LoxNativeClass("Number", ObjectClass){

    init{
        defineNativeMetaclass("Number class")
        defineNativeMethod("isInt", 0, this::isIntDef)
        defineNativeMethod("isNumber", 0, this::isNumberDef)
        defineNativeMethod("toString", 0, this::toStringDef)
    }

    override val arity = 1

    override fun call(interpreter: Interpreter, arguments: List<Any?>?) = arguments!![0] as? Double ?: 0.0

    private fun isIntDef(interpreter: Interpreter, arguments: List<Any?>?) = (LoxNumber.value % 1) == 0.0

    private fun isNumberDef(interpreter: Interpreter, arguments: List<Any?>?) = true

    private fun toStringDef(interpreter: Interpreter, arguments: List<Any?>?) = LoxNumber.value.toString()
}