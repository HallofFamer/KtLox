package com.mysidia.ktlox.std.func

import com.mysidia.ktlox.common.LoxNativeFunction

object Print : LoxNativeFunction("print", 1, { args ->
    print(args?.get(0).toString())
})