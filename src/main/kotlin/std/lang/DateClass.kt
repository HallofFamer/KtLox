package com.mysidia.ktlox.std.lang

import com.mysidia.ktlox.common.*
import com.mysidia.ktlox.interpreter.ArgumentError
import com.mysidia.ktlox.interpreter.Interpreter
import java.time.DateTimeException
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object DateClass : LoxNativeClass("Date", ObjectClass, null, DateMetaclass){

    init{
        defineNativeGetter("timestamp", this::timestampProp)
        defineNativeMethod("add", 1, this::addDef)
        defineNativeMethod("addDays", 1, this::addDaysDef)
        defineNativeMethod("diff", 1, this::diffDef)
        defineNativeMethod("diffDate", 1, this::diffDateDef)
        defineNativeMethod("format", 1, this::formatDef)
        defineNativeMethod("init", 3, this::initDef)
        defineNativeMethod("subtract", 1, this::subtractDef)
        defineNativeMethod("subtractDays", 1, this::subtractDaysDef)
        defineNativeMethod("toDateTime", 0, this::toDateTimeDef)
        defineNativeMethod("toString", 0, this::toStringDef)
    }

    override fun call(interpreter: Interpreter, arguments: List<Any?>?) = initDef(interpreter, arguments)

    private fun addDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxDate{
        val self = interpreter.thisInstance as LoxDate
        val duration = arguments!![0] as LoxObject
        val days = duration.getProperty("days") as Double
        return DateMetaclass.createFromLocalDate(self.jDate.plus(Period.ofDays(days.toInt())))
    }

    private fun addDaysDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxDate{
        val self = interpreter.thisInstance as LoxDate
        val days = arguments!![0] as? Double ?: throw ArgumentError("the argument for days to be added must be a number.")
        return DateMetaclass.createFromLocalDate(self.jDate.plusDays(days.toLong()))
    }

    private fun diffDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxObject{
        val self = interpreter.thisInstance as LoxDate
        val other = arguments!![0] as? LoxDate ?: throw ArgumentError("The argument must be an instance of Date")
        return DurationMetaClass.createFromDateDiff(self, other)
    }

    private fun diffDateDef(interpreter: Interpreter, arguments: List<Any?>?) : Long{
        val self = interpreter.thisInstance as LoxDate
        val other = arguments!![0] as? LoxDate ?: throw ArgumentError("The argument must be an instance of Date")
        return self.jDate.until(other.jDate, ChronoUnit.DAYS)
    }

    private fun formatDef(interpreter: Interpreter, arguments: List<Any?>?) : String{
        try {
            val self = interpreter.thisInstance as LoxDate
            val format = arguments!![0] as? String ?: throw ArgumentError("format argument must be a string!")
            return self.jDate.format(DateTimeFormatter.ofPattern(format))
        }
        catch(ex: DateTimeException){
            throw ArgumentError(ex.message!!)
        }
    }

    private fun initDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxDate{
        try{
            val year = arguments!![0] as? Double ?: throw ArgumentError("the argument for year must be a number.")
            val month = arguments[1] as? Double ?: throw ArgumentError("the argument for month must be a number.")
            val day = arguments[2] as? Double ?: throw ArgumentError("the argument for day must be a number.")
            return DateMetaclass.createFromLocalDate(LocalDate.of(year.toInt(), month.toInt(), day.toInt()))
        }
        catch(ex: DateTimeException){
            throw ArgumentError(ex.message!!)
        }
    }

    private fun subtractDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxDate{
        val self = interpreter.thisInstance as LoxDate
        val duration = arguments!![0] as LoxObject
        val days = duration.getProperty("days") as Double
        return DateMetaclass.createFromLocalDate(self.jDate.minus(Period.ofDays(days.toInt())))
    }

    private fun subtractDaysDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxDate{
        val self = interpreter.thisInstance as LoxDate
        val days = arguments!![0] as? Double ?: throw ArgumentError("the argument for days to be subtracted must be a number.")
        return DateMetaclass.createFromLocalDate(self.jDate.minusDays(days.toLong()))
    }

    private fun timestampProp(interpreter: Interpreter, arguments: List<Any?>?) = (interpreter.thisInstance as LoxDate).jDate.toEpochDay() * 86400000

    private fun toDateTimeDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxDateTime{
        val self = interpreter.thisInstance as LoxDate
        return LoxDateTime(self.jDate.atStartOfDay())
    }

    private fun toStringDef(interpreter: Interpreter, arguments: List<Any?>?) = (interpreter.thisInstance as LoxDate).toString()
}