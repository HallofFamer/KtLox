package com.mysidia.ktlox.common

import com.mysidia.ktlox.std.lang.TraitClass

class LoxTrait(val name: String,
               val methods: Map<String, LoxCallable>,
               val traits: List<LoxTrait>? = null) : LoxObject(TraitClass) {

    val parents : List<LoxTrait> by lazy {
        val parentTraits = mutableListOf<LoxTrait>()
        traits?.forEach {
            parentTraits.add(it)
            parentTraits.addAll(it.parents)
        }
        parentTraits
    }

    fun printParents() : String{
        var parentTraits = ""
        parents.forEach { parentTraits += "${it.name} " }
        return parentTraits
    }

    override fun toString() = name
}