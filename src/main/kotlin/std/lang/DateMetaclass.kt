package com.mysidia.ktlox.std.lang

import com.mysidia.ktlox.common.*
import com.mysidia.ktlox.interpreter.Interpreter
import java.time.LocalDate

object DateMetaclass : LoxNativeClass("Date class", ClassClass, null, ClassClass) {

    private val now : LoxDate by lazy {
        val jDate = LocalDate.now()
        createFromLocalDate(jDate)
    }

    init{
        defineNativeGetter("now", this::nowProp)
        defineNativeMethod("parse", 1, this::parseDef)
    }

    fun createFromLocalDate(jDate : LocalDate, cDate: LoxDate? = null) : LoxDate{
        val date = cDate ?: LoxDate(jDate)
        date.setProperty("day", jDate.dayOfMonth)
        date.setProperty("month", jDate.monthValue)
        date.setProperty("year", jDate.year)
        date.setProperty("dayOfWeek", jDate.dayOfWeek)
        date.setProperty("dayOfYear", jDate.dayOfYear)
        return date
    }

    private fun nowProp(interpreter: Interpreter, arguments: List<Any?>?) = now

    private fun parseDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxDate{
        val jDate = LocalDate.parse(arguments!![0] as String)
        return createFromLocalDate(jDate)
    }
}