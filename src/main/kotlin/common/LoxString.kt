package com.mysidia.ktlox.common

import com.mysidia.ktlox.std.lang.StringClass

object LoxString : LoxObject(StringClass) {

    var value : String = ""
    var length: Int = 0

    fun reset(value: String) : LoxString{
        this.value = value
        this.length = value.length
        return this
    }
}