package com.mysidia.ktlox.std.util

import com.mysidia.ktlox.common.LoxNativeFunction

object PrintLnFunc : LoxNativeFunction("println", 1,  { args ->
    println(args!![0])
})