package com.mysidia.ktlox.std.func

import com.mysidia.ktlox.common.LoxNativeFunction

object ReadLn : LoxNativeFunction("readLn", 0, {
    readLine()
})