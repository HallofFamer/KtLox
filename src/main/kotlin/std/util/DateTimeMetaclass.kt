package com.mysidia.ktlox.std.util

import com.mysidia.ktlox.common.*
import com.mysidia.ktlox.interpreter.Interpreter
import com.mysidia.ktlox.std.lang.ClassClass
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
        val dateTime = cDateTime ?: LoxDateTime()
        dateTime.jDateTime = jDateTime
        dateTime.setProperty("day", jDateTime.dayOfMonth.toLong())
        dateTime.setProperty("month", jDateTime.monthValue.toLong())
        dateTime.setProperty("year", jDateTime.year.toLong())
        dateTime.setProperty("hour", jDateTime.hour.toLong())
        dateTime.setProperty("minute", jDateTime.minute.toLong())
        dateTime.setProperty("second", jDateTime.second.toLong())
        dateTime.setProperty("dayOfWeek", jDateTime.dayOfWeek.value.toLong())
        dateTime.setProperty("dayOfYear", jDateTime.dayOfYear.toLong())
        dateTime.setProperty("nanosecond", jDateTime.nano.toLong())
        return dateTime
    }

    private fun nowProp(interpreter: Interpreter, arguments: List<Any?>?) = now

    private fun parseDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxDateTime{
        val jDateTime = LocalDateTime.parse(arguments!![0] as String)
        return createFromLocalDateTime(jDateTime)
    }
}