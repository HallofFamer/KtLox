package com.mysidia.ktlox.std.util

import com.mysidia.ktlox.common.*
import com.mysidia.ktlox.interpreter.ArgumentError
import com.mysidia.ktlox.interpreter.Interpreter
import com.mysidia.ktlox.std.lang.ObjectClass
import kotlin.random.Random

object RandomClass : LoxNativeClass("Random", ObjectClass){

    init {
        defineNativeMetaclass("Random class")
        defineNativeMethod("between", 2, this::betweenDef)
        defineNativeMethod("next", 0, this::nextDef)
        defineNativeMethod("nextInt", 1, this::nextIntDef)
    }

    override val arity = 0

    private fun betweenDef(interpreter: Interpreter, arguments: List<Any?>?) : Long{
        val start = arguments!![0] as? Long ?: throw ArgumentError("The first argument(lower bound) must be an integer.")
        val end = arguments[1] as? Long ?: throw ArgumentError("The second argument(upper bound) must be an integer.")
        return Random.nextInt((end - start).toInt() + 1).toLong() + start
    }

    private fun nextDef(interpreter: Interpreter, arguments: List<Any?>?) = Random.nextLong()

    private fun nextIntDef(interpreter: Interpreter, arguments: List<Any?>?) : Long{
        val bound = arguments!![0] as? Long ?: throw ArgumentError("The first argument(bound) must be an integer.")
        return Random.nextInt(bound.toInt()).toLong()
    }
}