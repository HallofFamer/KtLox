package com.mysidia.ktlox.common

class LoxTrait(val name: String,
               val methods: Map<String, LoxFunction>) {

    override fun toString() = name
}