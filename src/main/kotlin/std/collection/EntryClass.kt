package com.mysidia.ktlox.std.collection

import com.mysidia.ktlox.common.*
import com.mysidia.ktlox.interpreter.ArgumentError
import com.mysidia.ktlox.interpreter.Interpreter
import com.mysidia.ktlox.std.lang.ObjectClass

object EntryClass : LoxNativeClass("Entry", ObjectClass){

    init{
        defineNativeMetaclass("Entry class")
    }

    override val arity = 2

    override val isNative = true

    override fun initDef(interpreter: Interpreter, arguments: List<Any?>?): LoxEntry{
        val thisInstance = interpreter.thisInstance as? LoxEntry ?: LoxEntry()
        val key = arguments!![0] ?: throw ArgumentError("The key for entry(first argument) cannot be nil.")
        val value = arguments[1]
        thisInstance.key = key
        thisInstance.value = value
        return thisInstance
    }

    override fun new(klass: LoxClass) = LoxMap(klass)
}