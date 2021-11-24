package com.mysidia.ktlox.std.lang

import com.mysidia.ktlox.common.*
import com.mysidia.ktlox.interpreter.Interpreter
import kotlin.math.*

object FloatClass : LoxNativeClass("Float", NumberClass){

    init{
        defineNativeMetaclass("Float class")
        defineNativeMethod("abs", 0, this::absDef)
        defineNativeMethod("init", 1, this::initDef)
        defineNativeMethod("isFloat", 0, this::isFloatDef)
    }

    override val arity = 1

    override fun initDef(interpreter: Interpreter, arguments: List<Any?>?) = arguments!![0] as? Double ?: 0.0

    private fun absDef(interpreter: Interpreter, arguments: List<Any?>?) = abs(LoxFloat.value)

    private fun isFloatDef(interpreter: Interpreter, arguments: List<Any?>?) = true
}