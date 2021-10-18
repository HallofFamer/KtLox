package com.mysidia.ktlox.std.lang

import com.mysidia.ktlox.common.*
import com.mysidia.ktlox.interpreter.Interpreter

object DurationClass : LoxNativeClass("Duration", ObjectClass, null, DurationMetaClass){

    init{
        defineNativeMethod("init", 4, this::initDef)
        defineNativeMethod("toString", 0, this::toStringDef)
    }

    private fun initDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxObject{
        val self = interpreter.thisInstance
        self.setProperty("days", arguments!![0])
        self.setProperty("hours", arguments[1])
        self.setProperty("minutes", arguments[2])
        self.setProperty("seconds", arguments[3])
        return self
    }

    private fun toStringDef(interpreter: Interpreter, arguments: List<Any?>?) : String{
        val self = interpreter.thisInstance
        return "${self.getProperty("days")} days, ${self.getProperty("hours")} hours, ${self.getProperty("minutes")} mins, ${self.getProperty("seconds")} secs"
    }
}