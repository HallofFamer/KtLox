package com.mysidia.ktlox.std.collection

import com.mysidia.ktlox.common.*

class LoxMap(klass : LoxClass = MapClass) : LoxObject(klass){

    val length : Int
        get() = elements.size

    lateinit var elements : MutableMap<Any, Any?>

    constructor(elements: MutableMap<Any, Any?>, klass: LoxClass = MapClass) : this(klass){
        this.elements = elements
    }

    fun emptyCollection() = LoxMap(mutableMapOf(), klass!!)

    override fun toString(): String {
        val text = StringBuilder()
        text.append("Map[")
        var index = 0
        elements.forEach { (key, value) ->
            if(index > 0) text.append(", ")
            text.append("$key : ${value.toString()}")
            index++
        }
        text.append("]")
        return text.toString()
    }
}