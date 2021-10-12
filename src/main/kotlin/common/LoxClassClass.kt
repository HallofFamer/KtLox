package com.mysidia.ktlox.common

import com.mysidia.ktlox.interpreter.Interpreter

object LoxClassClass : LoxNativeClass("Class", LoxObjectClass, null) {

    init{
        defineNativeMethod("getSuperclass", 0, this::getSuperclassDef)
        defineNativeMethod("isClass", 0, this::isClassDef)
        defineNativeMethod("toString", 0, this::toStringDef)
    }

    private fun getSuperclassDef(interpreter: Interpreter, arguments: List<Any?>?) = (interpreter.thisInstance as LoxClass).superclass

    private fun isClassDef(interpreter: Interpreter, arguments: List<Any?>?) = true

    private fun toStringDef(interpreter: Interpreter, arguments: List<Any?>?) = interpreter.thisInstance.className
}