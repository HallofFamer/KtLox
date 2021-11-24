package com.mysidia.ktlox.std.util

import com.mysidia.ktlox.common.*
import com.mysidia.ktlox.interpreter.Interpreter
import com.mysidia.ktlox.std.lang.ClassClass
import java.time.LocalDate

object DateMetaclass : LoxNativeClass("Date class", ClassClass, null, ClassClass){

    private val now : LoxDate by lazy {
        val jDate = LocalDate.now()
        createFromLocalDate(jDate)
    }

    init{
        defineNativeGetter("now", this::nowProp)
        defineNativeMethod("parse", 1, this::parseDef)
    }

    fun createFromLocalDate(jDate : LocalDate, cDate: LoxDate? = null) : LoxDate{
        val date = cDate ?: LoxDate()
        date.jDate = jDate
        date.setProperty("day", jDate.dayOfMonth.toLong())
        date.setProperty("month", jDate.monthValue.toLong())
        date.setProperty("year", jDate.year.toLong())
        date.setProperty("dayOfWeek", jDate.dayOfWeek.value.toLong())
        date.setProperty("dayOfYear", jDate.dayOfYear.toLong())
        return date
    }

    private fun nowProp(interpreter: Interpreter, arguments: List<Any?>?) = now

    private fun parseDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxDate{
        val jDate = LocalDate.parse(arguments!![0] as String)
        return createFromLocalDate(jDate)
    }
}