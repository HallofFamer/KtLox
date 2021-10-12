package com.mysidia.ktlox.common

object LoxString : LoxObject(LoxStringClass) {

    var value : String = ""
    var length: Int = 0

    fun reset(value: String) : LoxString{
        this.value = value
        this.length = value.length
        return this
    }
}