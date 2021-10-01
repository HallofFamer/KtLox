package com.mysidia.ktlox.ext

import com.mysidia.ktlox.common.LoxCallable
import com.mysidia.ktlox.interpreter.Interpreter

object Clock : LoxCallable{

    override val arity = 0

    override fun call(interpreter: Interpreter, arguments: List<Any?>?) = System.currentTimeMillis() / 1000.0

    override fun toString() = "<native fn: clock>"
}