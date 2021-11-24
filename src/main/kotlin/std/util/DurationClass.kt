package com.mysidia.ktlox.std.util

import com.mysidia.ktlox.common.*
import com.mysidia.ktlox.interpreter.Interpreter
import com.mysidia.ktlox.std.lang.ObjectClass

object DurationClass : LoxNativeClass("Duration", ObjectClass, null, DurationMetaclass){

    init{
        defineNativeMethod("init", 4, this::initDef)
        defineNativeMethod("toString", 0, this::toStringDef)
    }

    override val arity = 4

    override fun initDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxObject{
        val self = thisInstance(interpreter)
        self.setProperty("days", arguments!![0] as Long)
        self.setProperty("hours", arguments[1] as Long)
        self.setProperty("minutes", arguments[2] as Long)
        self.setProperty("seconds", arguments[3] as Long)
        return self
    }

    private fun toStringDef(interpreter: Interpreter, arguments: List<Any?>?) : String{
        val self = interpreter.thisInstance
        return "${self.getProperty("days")} days, ${self.getProperty("hours")} hours, ${self.getProperty("minutes")} mins, ${self.getProperty("seconds")} secs."
    }
}