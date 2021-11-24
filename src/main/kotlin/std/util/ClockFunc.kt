package com.mysidia.ktlox.std.util

import com.mysidia.ktlox.common.LoxNativeFunction

object ClockFunc : LoxNativeFunction("clock", 0, {
    System.currentTimeMillis() / 1000.0
})