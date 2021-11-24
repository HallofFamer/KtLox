package com.mysidia.ktlox.std.collection

import com.mysidia.ktlox.common.*

class LoxSet(klass: LoxClass = SetClass) : LoxObject(klass), LoxCollection<MutableSet<Any?>>{

    override lateinit var elements: MutableSet<Any?>

    constructor(elements: MutableSet<Any?>, klass: LoxClass = SetClass) : this(klass){
        this.elements = elements
    }

    override fun emptyCollection() = LoxSet(mutableSetOf(), klass!!)

    override fun toString(): String{
        val text = StringBuilder()
        text.append("Set[")
        elements.forEachIndexed { index, element ->
            if(index > 0) text.append(", ")
            text.append(element?.toString() ?: "nil")
        }
        text.append("]")
        return text.toString()
    }
}