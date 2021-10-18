package com.mysidia.ktlox.std.lang

import com.mysidia.ktlox.common.*
import com.mysidia.ktlox.interpreter.Interpreter

object ClassClass : LoxNativeClass("Class", ObjectClass, null) {

    init{
        klass = this
        defineNativeMethod("getSuperclass", 0, this::getSuperclassDef)
        defineNativeMethod("getTraits", 0, this::getTraitsDef)
        defineNativeMethod("isClass", 0, this::isClassDef)
        defineNativeMethod("toString", 0, this::toStringDef)
    }

    private fun getSuperclassDef(interpreter: Interpreter, arguments: List<Any?>?) = (interpreter.thisInstance as LoxClass).superclass

    private fun getTraitsDef(interpreter: Interpreter, arguments: List<Any?>?) : String{
        val self = interpreter.thisInstance as LoxClass
        var traits = ""
        self.traits?.forEach { trait ->
            traits += "${trait.name} " + trait.printParents()
        }
        return traits
    }

    private fun isClassDef(interpreter: Interpreter, arguments: List<Any?>?) = true

    private fun toStringDef(interpreter: Interpreter, arguments: List<Any?>?) = interpreter.thisInstance.className
}