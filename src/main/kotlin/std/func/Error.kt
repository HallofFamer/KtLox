package com.mysidia.ktlox.std.func

import com.mysidia.ktlox.common.LoxNativeFunction
import kotlin.system.exitProcess

object Error : LoxNativeFunction("error", 1, { args ->
    System.err.print(args?.get(0).toString())
    exitProcess(70)
})