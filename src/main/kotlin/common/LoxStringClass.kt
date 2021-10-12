package com.mysidia.ktlox.common

import com.mysidia.ktlox.interpreter.Interpreter

object LoxStringClass : LoxNativeClass("String", LoxObjectClass) {

    init{
        defineNativeMetaclass("String class")
        defineNativeGetter("length", this::lengthProp)
        defineNativeMethod("isEmpty", 0, this::isEmptyDef)
        defineNativeMethod("isNilOrEmpty", 0, this::isNilOrEmptyDef)
        defineNativeMethod("isString", 0, this::isStringDef)
        defineNativeMethod("toString", 0, this::toStringDef)
    }

    override val arity = 1

    override fun call(interpreter: Interpreter, arguments: List<Any?>?) = arguments!![0] as? String ?: ""

    private fun isEmptyDef(interpreter: Interpreter, arguments: List<Any?>?) = (interpreter.thisInstance as LoxString).length == 0

    private fun isNilOrEmptyDef(interpreter: Interpreter, arguments: List<Any?>?) = isEmptyDef(interpreter, arguments)

    private fun isStringDef(interpreter: Interpreter, arguments: List<Any?>?) = true

    private fun lengthProp(interpreter: Interpreter, arguments: List<Any?>?) = (interpreter.thisInstance as LoxString).length

    private fun toStringDef(interpreter: Interpreter, arguments: List<Any?>?) = (interpreter.thisInstance as LoxString).value
}