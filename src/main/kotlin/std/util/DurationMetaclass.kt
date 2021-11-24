package com.mysidia.ktlox.std.util

import com.mysidia.ktlox.common.*
import com.mysidia.ktlox.interpreter.Interpreter
import com.mysidia.ktlox.std.lang.ClassClass
import java.time.temporal.ChronoUnit

object DurationMetaclass : LoxNativeClass("Duration class", ClassClass, null, ClassClass){

    init{
        defineNativeMethod("ofDays", 1, this::ofDaysDef)
        defineNativeMethod("ofHours", 1, this::ofHoursDef)
        defineNativeMethod("ofMinutes", 1, this::ofMinutesDef)
        defineNativeMethod("ofSeconds", 1, this::ofSecondsDef)
    }

    fun createFromDateDiff(self: LoxDate, other: LoxDate) : LoxObject{
        val days = self.jDate.until(other.jDate, ChronoUnit.DAYS)
        val duration = LoxObject(DurationClass)
        duration.setProperty("days", days)
        duration.setProperty("hours", 0L)
        duration.setProperty("minutes", 0L)
        duration.setProperty("seconds", 0L)
        return duration
    }

    fun createFromTimeDiff(self: LoxDateTime, other: LoxDateTime) : LoxObject{
        var time = self.jDateTime.until(other.jDateTime, ChronoUnit.SECONDS)
        val days = time / 86400
        time %= 86400
        val hours = time / 3600
        time %= 3600
        val minutes = time / 60
        time %= 60
        val seconds = time

        val duration = LoxObject(DurationClass)
        duration.setProperty("days", days)
        duration.setProperty("hours", hours)
        duration.setProperty("minutes", minutes)
        duration.setProperty("seconds", seconds)
        return duration
    }

    private fun ofDaysDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxObject{
        val self = LoxObject(DurationClass)
        self.setProperty("days", arguments!![0] as Long)
        self.setProperty("hours", 0L)
        self.setProperty("minutes", 0L)
        self.setProperty("seconds", 0L)
        return self
    }

    private fun ofHoursDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxObject{
        val self = LoxObject(DurationClass)
        self.setProperty("days", 0L)
        self.setProperty("hours", arguments!![0] as Long)
        self.setProperty("minutes", 0L)
        self.setProperty("seconds", 0L)
        return self
    }

    private fun ofMinutesDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxObject{
        val self = LoxObject(DurationClass)
        self.setProperty("days", 0L)
        self.setProperty("hours", 0L)
        self.setProperty("minutes", arguments!![0] as Long)
        self.setProperty("seconds", 0L)
        return self
    }

    private fun ofSecondsDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxObject{
        val self = LoxObject(DurationClass)
        self.setProperty("days", 0L)
        self.setProperty("hours", 0L)
        self.setProperty("minutes", 0L)
        self.setProperty("seconds", arguments!![0] as Long)
        return self
    }
}