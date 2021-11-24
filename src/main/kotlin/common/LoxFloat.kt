package com.mysidia.ktlox.common

import com.mysidia.ktlox.std.lang.FloatClass

object LoxFloat : LoxObject(FloatClass), LoxNumeric<Double> {

    override var value : Double = 0.0

    override fun reset(value: Double) : LoxFloat{
        this.value = value
        LoxNumber.reset(value)
        return this
    }
}