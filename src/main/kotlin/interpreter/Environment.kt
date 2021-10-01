package com.mysidia.ktlox.interpreter

import com.mysidia.ktlox.lexer.Token

class Environment(val enclosing: Environment? = null) {

    private val variables = mutableMapOf<String, Any?>()

    fun assign(name: Token, value: Any?) {
        when{
            variables.containsKey(name.lexeme) -> variables[name.lexeme] = value
            enclosing != null -> enclosing.assign(name, value)
            else -> throw RuntimeError(name, "Undefined Variable '${name.lexeme}'.")
        }
    }

    fun assignAt(distance: Int, name: Token, value: Any?) {
        ancestor(distance).variables[name.lexeme] = value
    }

    fun define(name: String, value: Any?){
        variables[name] = value
    }

    fun get(name: Token) : Any? {
        return when{
            variables.containsKey(name.lexeme) -> {
                val value = variables[name.lexeme]
                if(value == Unassigned) throw RuntimeError(name, "Variable ${name.lexeme} accessed prior to assignment!")
                else value
            }
            enclosing != null -> enclosing.get(name)
            else -> throw RuntimeError(name, "Undefined Variable '${name.lexeme}'.")
        }
    }

    fun getAt(distance: Int, name: String) = ancestor(distance).variables[name]

    private fun ancestor(distance: Int) : Environment{
        var environment = this
        for(i in 0 until distance) environment = environment.enclosing!!
        return environment
    }

    object Unassigned
}