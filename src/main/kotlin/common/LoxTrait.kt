package com.mysidia.ktlox.common

class LoxTrait(val name: String,
               val methods: Map<String, LoxCallable>,
               val parents: List<LoxTrait>? = null) : LoxObject(LoxTraitClass) {

    override fun toString() = name
}