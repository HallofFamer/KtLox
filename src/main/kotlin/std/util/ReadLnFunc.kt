package com.mysidia.ktlox.std.util

import com.mysidia.ktlox.common.LoxNativeFunction

object ReadLnFunc : LoxNativeFunction("readLn", 0, {
    readLine()
})