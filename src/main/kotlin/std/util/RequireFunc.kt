package com.mysidia.ktlox.std.util

import com.mysidia.ktlox.Lox
import com.mysidia.ktlox.common.LoxNativeFunction

object RequireFunc : LoxNativeFunction("require", 1, { args ->
    Lox.runFile(args!![0] as String)
})