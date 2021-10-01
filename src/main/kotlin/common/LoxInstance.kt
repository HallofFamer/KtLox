package com.mysidia.ktlox.common

import com.mysidia.ktlox.interpreter.RuntimeError
import com.mysidia.ktlox.lexer.Token

open class LoxInstance(private var klass: LoxClass?) {

    private val fields = mutableMapOf<String, Any?>()

    fun get(name: Token) : Any? {
        if(fields.containsKey(name.lexeme)) return fields[name.lexeme]
        val method = klass?.findMethod(name.lexeme)
        if(method != null) return method.bind(this)
        throw RuntimeError(name, "Undefined property ${name.lexeme}.")
    }

    fun set(name: Token, value: Any?){
        fields[name.lexeme] = value
    }

    override fun toString() = "${klass?.name} instance"
}