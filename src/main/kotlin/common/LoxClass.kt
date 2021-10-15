package com.mysidia.ktlox.common

import com.mysidia.ktlox.interpreter.Interpreter

open class LoxClass(val name: String,
                    val superclass: LoxClass?,
                    val methods: MutableMap<String, LoxCallable>,
                    val traits: MutableList<LoxTrait>? = null,
                    metaclass: LoxClass? = null) : LoxObject(metaclass), LoxCallable{

    private val initializer by lazy { findMethod("init") }
    override val arity get() = initializer?.arity ?: 0
    override val isGetter = false

    override fun call(interpreter: Interpreter, arguments: List<Any?>?): Any {
        val instance = LoxObject(this)
        initializer?.bind(instance)?.call(interpreter, arguments)
        return instance
    }

    fun findMethod(name: String) : LoxCallable? {
        if(methods.containsKey(name)) return methods[name]
        return superclass?.findMethod(name)
    }

    override fun toString() = name
}