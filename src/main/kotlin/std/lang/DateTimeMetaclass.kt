package com.mysidia.ktlox.std.lang

import com.mysidia.ktlox.common.*
import com.mysidia.ktlox.interpreter.Interpreter
import java.time.LocalDateTime

object DateTimeMetaclass : LoxNativeClass("DateTime class", ClassClass, null, ClassClass){

    private val now : LoxDateTime by lazy {
        val jDateTime = LocalDateTime.now()
        createFromLocalDateTime(jDateTime)
    }

    init{
        defineNativeGetter("now", this::nowProp)
        defineNativeMethod("parse", 1, this::parseDef)
    }

    fun createFromLocalDateTime(jDateTime : LocalDateTime, cDateTime: LoxDateTime? = null) : LoxDateTime{
        val dateTime = cDateTime ?: LoxDateTime(jDateTime)
        dateTime.setProperty("day", jDateTime.dayOfMonth)
        dateTime.setProperty("month", jDateTime.monthValue)
        dateTime.setProperty("year", jDateTime.year)
        dateTime.setProperty("hour", jDateTime.hour)
        dateTime.setProperty("minute", jDateTime.minute)
        dateTime.setProperty("second", jDateTime.second)
        dateTime.setProperty("dayOfWeek", jDateTime.dayOfWeek)
        dateTime.setProperty("dayOfYear", jDateTime.dayOfYear)
        dateTime.setProperty("nanosecond", jDateTime.nano)
        return dateTime
    }

    private fun nowProp(interpreter: Interpreter, arguments: List<Any?>?) = now

    private fun parseDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxDateTime {
        val jDateTime = LocalDateTime.parse(arguments!![0] as String)
        return createFromLocalDateTime(jDateTime)
    }
}