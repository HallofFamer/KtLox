package com.mysidia.ktlox.common

import com.mysidia.ktlox.interpreter.Interpreter

interface LoxCallable {
    val arity : Int
    fun call(interpreter: Interpreter, arguments: List<Any?>?) : Any?
}