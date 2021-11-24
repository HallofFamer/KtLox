package com.mysidia.ktlox.common

import com.mysidia.ktlox.std.lang.NumberClass

object LoxNumber : LoxObject(NumberClass), LoxNumeric<Number>{

    override var value : Number = 0.0

    override fun reset(value: Number) : LoxNumber{
        this.value = value
        return this
    }
}