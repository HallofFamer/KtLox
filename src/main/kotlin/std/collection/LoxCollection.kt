package com.mysidia.ktlox.std.collection

interface LoxCollection<T: MutableCollection<Any?>>{

    val length : Int
        get() = elements.size

    var elements : T

    fun emptyCollection() : LoxCollection<T>
}