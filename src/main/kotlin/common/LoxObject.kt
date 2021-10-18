package com.mysidia.ktlox.common

import com.mysidia.ktlox.interpreter.RuntimeError
import com.mysidia.ktlox.lexer.Token

open class LoxObject(var klass: LoxClass?) {

    private val fields = mutableMapOf<String, Any?>()
    val className by lazy { klass?.name ?: "Object" }

    fun get(name: Token) : Any? {
        if(fields.containsKey(name.lexeme)) return fields[name.lexeme]
        val method = klass?.findMethod(name.lexeme)
        if(method != null) return method.bind(this)
        throw RuntimeError(name, "Undefined property ${name.lexeme}.")
    }

    fun getProperty(name: String) = fields[name]

    fun set(name: Token, value: Any?){
        fields[name.lexeme] = value
    }

    fun setProperty(name: String, value: Any?){
        fields[name] = value
    }

    override fun toString() = "$className instance"
}