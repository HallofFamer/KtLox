package com.mysidia.ktlox.std.io

import com.mysidia.ktlox.common.*

object PrintFunc : LoxNativeFunction("print", 1, { args ->
    print(args?.get(0))
})