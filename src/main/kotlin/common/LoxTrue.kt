package com.mysidia.ktlox.common

import com.mysidia.ktlox.std.lang.TrueClass

object LoxTrue : LoxObject(TrueClass), LoxPrimitive<Boolean>{
    override val value = true
}