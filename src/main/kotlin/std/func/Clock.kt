package com.mysidia.ktlox.std.func

import com.mysidia.ktlox.common.LoxNativeFunction

object Clock : LoxNativeFunction("clock", 0, {
    System.currentTimeMillis() / 1000.0
})