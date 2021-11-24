package com.mysidia.ktlox.std.util

import com.mysidia.ktlox.common.*

object PrintFunc : LoxNativeFunction("print", 1, { args ->
    print(args?.get(0))
})