package com.mysidia.ktlox.common

interface LoxNumeric<T : Number> {
    var value : T
    fun reset(value: T) : LoxObject
}