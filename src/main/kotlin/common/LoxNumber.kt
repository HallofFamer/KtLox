package com.mysidia.ktlox.common

object LoxNumber : LoxObject(LoxNumberClass){

    var value : Double = 0.0

    fun reset(value: Double) : LoxNumber{
        this.value = value
        return this
    }
}