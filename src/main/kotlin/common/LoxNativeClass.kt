package com.mysidia.ktlox.common

import com.mysidia.ktlox.interpreter.Interpreter
import com.mysidia.ktlox.std.lang.ClassClass

abstract class LoxNativeClass(name: String,
                              superclass: LoxClass?,
                              traits: MutableList<LoxTrait>? = null,
                              metaclass: LoxClass? = null) : LoxClass(name, superclass, mutableMapOf(), traits, metaclass){

    fun defineNativeGetter(name: String, body: (interpreter : Interpreter, arguments: List<Any?>?) -> Any?){
        methods[name] = LoxNativeMethod(name, 0, true, null, body)
    }

    fun defineNativeMetaclass(name: String, superclass: LoxClass? = ClassClass){
        klass = LoxClass(name, superclass, mutableMapOf(), null, ClassClass)
    }

    fun defineNativeMethod(name: String, arity: Int, body: (interpreter : Interpreter, arguments: List<Any?>?) -> Any?){
        methods[name] = LoxNativeMethod(name, arity, false,null, body)
    }
}