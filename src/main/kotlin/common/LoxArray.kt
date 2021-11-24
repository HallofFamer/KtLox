package com.mysidia.ktlox.common

import com.mysidia.ktlox.std.lang.ArrayClass

class LoxArray(klass: LoxClass = ArrayClass) : LoxObject(klass) {

    var length = 0
    lateinit var elements : Array<Any?>

    constructor(length: Int, klass: LoxClass = ArrayClass) : this(klass){
        this.length = length
    }

    constructor(elements : Array<Any?>, klass: LoxClass = ArrayClass) : this(klass){
        this.length = elements.size
        this.elements = elements
    }

    override fun toString(): String {
        val text = StringBuilder()
        text.append("Array[")
        elements.forEachIndexed { index, element ->
            if(index > 0) text.append(", ")
            text.append(element?.toString() ?: "nil")
        }
        text.append("]")
        return text.toString()
    }
}