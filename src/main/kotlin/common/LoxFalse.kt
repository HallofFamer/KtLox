package com.mysidia.ktlox.common

import com.mysidia.ktlox.std.lang.FalseClass

object LoxFalse : LoxObject(FalseClass), LoxPrimitive<Boolean>{
    override val value = false
}