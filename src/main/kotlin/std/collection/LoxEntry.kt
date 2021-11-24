package com.mysidia.ktlox.std.collection

import com.mysidia.ktlox.common.*

class LoxEntry(klass: LoxClass = EntryClass) : LoxObject(klass){

    lateinit var key : Any
    var value : Any? = null

    constructor(key: Any, value: Any?, klass: LoxClass = EntryClass) : this(klass){
        this.key = key
        this.value = value
    }

    constructor(entry: MutableMap.MutableEntry<Any, Any?>, klass: LoxClass = EntryClass) : this(klass){
        this.key = entry.key
        this.value = entry.value
    }

    override fun toString(): String {
        return "Entry(key: $key, value: $value)"
    }
}