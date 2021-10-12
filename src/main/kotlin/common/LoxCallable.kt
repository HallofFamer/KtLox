package com.mysidia.ktlox.common

import com.mysidia.ktlox.interpreter.Interpreter

interface LoxCallable {
    val arity : Int
    val isGetter : Boolean

    fun bind(instance: LoxObject) : LoxCallable = this
    fun call(interpreter: Interpreter, arguments: List<Any?>?) : Any?
}