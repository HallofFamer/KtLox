package com.mysidia.ktlox.common

import com.mysidia.ktlox.interpreter.Interpreter

object LoxTraitClass : LoxNativeClass("Trait", LoxObjectClass){

    init{
        defineNativeMetaclass("Trait class")
        defineNativeMethod("parents", 0, this::parentsDef)
    }

    private fun parentsDef(interpreter: Interpreter, arguments: List<Any?>?) : String{
        val self = interpreter.thisInstance as LoxTrait
        var parents = ""
        self.parents?.forEach {
            parents += "${it.name} "
        }
        return parents
    }
}