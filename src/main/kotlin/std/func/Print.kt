package com.mysidia.ktlox.std.func

import com.mysidia.ktlox.common.*
import com.mysidia.ktlox.interpreter.RuntimeError

object Print : LoxNativeFunction("print", 1, { args ->
    print(args?.get(0))
})