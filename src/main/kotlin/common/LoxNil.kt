package com.mysidia.ktlox.common

import com.mysidia.ktlox.std.lang.NilClass

object LoxNil : LoxObject(NilClass), LoxPrimitive<Nothing?>{
    override val value: Nothing? = null
}