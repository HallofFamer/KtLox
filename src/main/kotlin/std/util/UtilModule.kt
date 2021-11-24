package com.mysidia.ktlox.std.util

import com.mysidia.ktlox.common.LoxModule
import com.mysidia.ktlox.interpreter.Environment

object UtilModule : LoxModule{

    override fun registerModule(env: Environment){
        env.define("clock", ClockFunc)
        env.define("error", ErrorFunc)
        env.define("require", RequireFunc)
        env.define("Date", DateClass)
        env.define("DateTime", DateTimeClass)
        env.define("Duration", DurationClass)
        env.define("Random", RandomClass)
    }
}