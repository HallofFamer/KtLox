package com.mysidia.ktlox.std.lang

import com.mysidia.ktlox.common.*
import com.mysidia.ktlox.interpreter.Interpreter

object TraitClass : LoxNativeClass("Trait", ObjectClass){

    init{
        defineNativeMetaclass("Trait class")
        defineNativeMethod("getParentTraits", 0, this::getParentTraitsDef)
        defineNativeMethod("isTrait", 0, this::isTraitDef)
    }

    private fun getParentTraitsDef(interpreter: Interpreter, arguments: List<Any?>?) : String{
        val self = interpreter.thisInstance as LoxTrait
        var parents = ""
        self.traits?.forEach { trait ->
            parents += "${trait.name} " + trait.printParents()
        }
        return parents
    }

    private fun isTraitDef(interpreter: Interpreter, arguments: List<Any?>?) = true
}