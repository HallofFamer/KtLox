package com.mysidia.ktlox.common

import com.mysidia.ktlox.interpreter.Interpreter

class LoxClass(val name: String,
               private val superclass: LoxClass?,
               private val methods: MutableMap<String, LoxFunction>,
               metaclass: LoxClass?) : LoxInstance(metaclass), LoxCallable{

    private val initializer by lazy { findMethod("init") }
    override val arity get() = initializer?.arity ?: 0

    override fun call(interpreter: Interpreter, arguments: List<Any?>?): Any {
        val instance = LoxInstance(this)
        initializer?.let { it.bind(instance).call(interpreter, arguments) }
        return instance
    }

    fun findMethod(name: String) : LoxFunction? {
        if(methods.containsKey(name)) return methods[name]
        return superclass?.findMethod(name)
    }

    override fun toString() = name
}