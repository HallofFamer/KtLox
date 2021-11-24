package com.mysidia.ktlox.common

import com.mysidia.ktlox.interpreter.Interpreter
import com.mysidia.ktlox.std.lang.ObjectClass

open class LoxClass(val name: String,
                    val superclass: LoxClass?,
                    val methods: MutableMap<String, LoxCallable>,
                    val traits: MutableList<LoxTrait>? = null,
                    metaclass: LoxClass? = null) : LoxObject(metaclass), LoxCallable{

    private val initializer by lazy { findMethod("init") }
    override val arity get() = initializer?.arity ?: 0
    override val isGetter = false
    open val isNative : Boolean = superclass?.isNative ?: false

    override fun call(interpreter: Interpreter, arguments: List<Any?>?): Any{
        val instance = new(this)
        initializer?.bind(instance)?.call(interpreter, arguments)
        return instance
    }

    fun findMethod(name: String) : LoxCallable?{
        if(methods.containsKey(name)) return methods[name]
        return superclass?.findMethod(name)
    }

    open fun new(klass: LoxClass) : LoxObject{
        return if(isNative) nativeSuperclass().new(klass) else LoxObject(klass)
    }

    private fun nativeSuperclass() : LoxClass{
        if(superclass == null) return ObjectClass
        var klass = superclass
        while(klass != null){
            if(klass.isNative) return klass
            klass = klass.superclass
        }
        return ObjectClass
    }

    override fun toString() = name
}