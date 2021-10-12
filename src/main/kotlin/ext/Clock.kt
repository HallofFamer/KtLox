package com.mysidia.ktlox.ext

import com.mysidia.ktlox.common.LoxNativeFunction

object Clock : LoxNativeFunction("clock", 0, {
    System.currentTimeMillis() / 1000.0
})