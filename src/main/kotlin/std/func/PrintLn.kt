package com.mysidia.ktlox.std.func

import com.mysidia.ktlox.common.LoxNativeFunction

object PrintLn : LoxNativeFunction("println", 1,  { args ->
    println(args?.get(0))
})