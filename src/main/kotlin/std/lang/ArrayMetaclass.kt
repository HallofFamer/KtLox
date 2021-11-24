package com.mysidia.ktlox.std.lang

import com.mysidia.ktlox.common.*
import com.mysidia.ktlox.interpreter.ArgumentError
import com.mysidia.ktlox.interpreter.Interpreter

object ArrayMetaclass : LoxNativeClass("Array class", ClassClass, null, ClassClass) {

    init{
        defineNativeMethod("fromArray", 1, this::fromArrayDef)
    }

    fun createFromArray(array: Array<Any?>) = LoxArray(array)

    fun createFromCollection(collection: Collection<Any?>) = LoxArray(collection.toTypedArray())

    fun createFromList(list: List<Any?>) = LoxArray(list.toTypedArray())

    fun createFromVarArgs(vararg elements: Any?) : LoxArray{
        return LoxArray(arrayOf(*elements))
    }

    private fun fromArrayDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxArray{
        val array = arguments!![0] as? LoxArray ?: throw ArgumentError("the argument for fromArray must be an instance of array.")
        return createFromArray(array.elements)
    }
}