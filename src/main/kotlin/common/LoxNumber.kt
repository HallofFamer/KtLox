package com.mysidia.ktlox.common

import com.mysidia.ktlox.std.lang.NumberClass

object LoxNumber : LoxObject(NumberClass){

    var value : Double = 0.0

    fun reset(value: Double) : LoxNumber{
        this.value = value
        return this
    }
}