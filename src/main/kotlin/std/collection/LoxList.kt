package com.mysidia.ktlox.std.collection

import com.mysidia.ktlox.common.*

class LoxList(klass: LoxClass = ListClass) : LoxObject(klass), LoxCollection<MutableList<Any?>> {

    override lateinit var elements : MutableList<Any?>

    constructor(elements: MutableList<Any?>, klass: LoxClass = ListClass) : this(klass){
        this.elements = elements
    }

    override fun emptyCollection() = LoxList(mutableListOf(), klass!!)

    override fun toString(): String {
        val text = StringBuilder()
        text.append("List[")
        elements.forEachIndexed { index, element ->
            if(index > 0) text.append(", ")
            text.append(element?.toString() ?: "nil")
        }
        text.append("]")
        return text.toString()
    }
}