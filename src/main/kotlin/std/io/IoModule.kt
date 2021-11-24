package com.mysidia.ktlox.std.io

import com.mysidia.ktlox.common.LoxModule
import com.mysidia.ktlox.interpreter.Environment

object IoModule : LoxModule {

    override fun registerModule(env: Environment) {
        env.define("File", FileClass)
        env.define("BinaryStream", BinaryStreamClass)
        env.define("FileStream", FileStreamClass)
    }
}