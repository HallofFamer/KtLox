package com.mysidia.ktlox.common

import com.mysidia.ktlox.std.lang.IntClass

object LoxInt : LoxObject(IntClass), LoxNumeric<Long> {

    override var value : Long = 0L

    fun gcd(other: Long) : Long{
        var gcd = 1L
        var i = 1L
        while(i <= value && i <= other){
            if(value % i == 0L && other % i == 0L) gcd = i
            ++i
        }
        return gcd
    }

    override fun reset(value: Long) : LoxInt{
        this.value = value
        LoxNumber.reset(value)
        return this
    }
}