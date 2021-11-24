package com.mysidia.ktlox.std.util

import com.mysidia.ktlox.common.LoxNativeFunction
import kotlin.system.exitProcess

object ErrorFunc : LoxNativeFunction("error", 1, { args ->
    System.err.println(args?.get(0).toString())
    exitProcess(70)
})