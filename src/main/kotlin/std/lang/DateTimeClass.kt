package com.mysidia.ktlox.std.lang

import com.mysidia.ktlox.common.*
import com.mysidia.ktlox.interpreter.ArgumentError
import com.mysidia.ktlox.interpreter.Interpreter
import java.sql.Timestamp
import java.time.DateTimeException
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object DateTimeClass : LoxNativeClass("DateTime", ObjectClass, null, DateTimeMetaclass) {

    init{
        defineNativeGetter("timestamp", this::timestampProp)
        defineNativeMethod("add", 1, this::addDef)
        defineNativeMethod("addDays", 1, this::addDaysDef)
        defineNativeMethod("addHours", 1, this::addHoursDef)
        defineNativeMethod("addMinutes", 1, this::addMinutesDef)
        defineNativeMethod("addSeconds", 1, this::addSecondsDef)
        defineNativeMethod("diff", 1, this::diffDef)
        defineNativeMethod("diffTime", 1, this::diffTimeDef)
        defineNativeMethod("format", 1, this::formatDef)
        defineNativeMethod("init", 6, this::initDef)
        defineNativeMethod("subtract", 1, this::subtractDef)
        defineNativeMethod("subtractDays", 1, this::subtractDaysDef)
        defineNativeMethod("subtractHours", 1, this::subtractHoursDef)
        defineNativeMethod("subtractMinutes", 1, this::subtractMinutesDef)
        defineNativeMethod("subtractSeconds", 1, this::subtractSecondsDef)
        defineNativeMethod("toDate", 0, this::toDateDef)
        defineNativeMethod("toString", 0, this::toStringDef)
    }

    override fun call(interpreter: Interpreter, arguments: List<Any?>?) = initDef(interpreter, arguments)

    private fun addDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxDateTime{
        val self = interpreter.thisInstance as LoxDateTime
        val text = textFromDuration(arguments!![0] as LoxObject)
        return DateTimeMetaclass.createFromLocalDateTime(self.jDateTime.plus(Duration.parse(text)))
    }

    private fun addDaysDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxDateTime{
        val self = interpreter.thisInstance as LoxDateTime
        val days = arguments!![0] as? Double ?: throw ArgumentError("the argument for days to be added must be a number.")
        return DateTimeMetaclass.createFromLocalDateTime(self.jDateTime.plusDays(days.toLong()))
    }

    private fun addHoursDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxDateTime{
        val self = interpreter.thisInstance as LoxDateTime
        val hours = arguments!![0] as? Double ?: throw ArgumentError("the argument for hours to be added must be a number.")
        return DateTimeMetaclass.createFromLocalDateTime(self.jDateTime.plusHours(hours.toLong()))
    }

    private fun addMinutesDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxDateTime{
        val self = interpreter.thisInstance as LoxDateTime
        val minutes = arguments!![0] as? Double ?: throw ArgumentError("the argument for minutes to be added must be a number.")
        return DateTimeMetaclass.createFromLocalDateTime(self.jDateTime.plusMinutes(minutes.toLong()))
    }

    private fun addSecondsDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxDateTime{
        val self = interpreter.thisInstance as LoxDateTime
        val seconds = arguments!![0] as? Double ?: throw ArgumentError("the argument for seconds to be added must be a number.")
        return DateTimeMetaclass.createFromLocalDateTime(self.jDateTime.plusSeconds(seconds.toLong()))
    }

    private fun diffDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxObject{
        val self = interpreter.thisInstance as LoxDateTime
        val other = arguments!![0] as? LoxDateTime ?: throw ArgumentError("The argument must be an instance of DateTime")
        return DurationMetaClass.createFromTimeDiff(self, other)
    }

    private fun diffTimeDef(interpreter: Interpreter, arguments: List<Any?>?) : Long{
        val self = interpreter.thisInstance as LoxDateTime
        val other = arguments!![0] as? LoxDateTime ?: throw ArgumentError("The argument must be an instance of DateTime")
        return self.jDateTime.until(other.jDateTime, ChronoUnit.SECONDS)
    }

    private fun formatDef(interpreter: Interpreter, arguments: List<Any?>?) : String{
        try {
            val self = interpreter.thisInstance as LoxDateTime
            val format = arguments!![0] as? String ?: throw ArgumentError("format argument must be a string!")
            return self.jDateTime.format(DateTimeFormatter.ofPattern(format))
        }
        catch(ex: DateTimeException){
            throw ArgumentError(ex.message!!)
        }
    }

    private fun initDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxDateTime{
        try{
            val year = arguments!![0] as? Double ?: throw ArgumentError("the argument for year must be a number.")
            val month = arguments[1] as? Double ?: throw ArgumentError("the argument for month must be a number.")
            val day = arguments[2] as? Double ?: throw ArgumentError("the argument for day must be a number.")
            val hour = arguments[3] as? Double ?: throw ArgumentError("the argument for hour must be a number.")
            val minute = arguments[4] as? Double ?: throw ArgumentError("the argument for minute must be a number.")
            val second = arguments[5] as? Double ?: throw ArgumentError("the argument for second must be a number.")
            return DateTimeMetaclass.createFromLocalDateTime(LocalDateTime.of(year.toInt(), month.toInt(), day.toInt(), hour.toInt(), minute.toInt(), second.toInt()))
        }
        catch(ex: DateTimeException){
            throw ArgumentError(ex.message!!)
        }
    }

    private fun subtractDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxDateTime{
        val self = interpreter.thisInstance as LoxDateTime
        val text = textFromDuration(arguments!![0] as LoxObject)
        return DateTimeMetaclass.createFromLocalDateTime(self.jDateTime.minus(Duration.parse(text)))
    }

    private fun subtractDaysDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxDateTime{
        val self = interpreter.thisInstance as LoxDateTime
        val days = arguments!![0] as? Double ?: throw ArgumentError("the argument for days to be subtracted must be a number.")
        return DateTimeMetaclass.createFromLocalDateTime(self.jDateTime.minusDays(days.toLong()))
    }

    private fun subtractHoursDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxDateTime{
        val self = interpreter.thisInstance as LoxDateTime
        val hours = arguments!![0] as? Double ?: throw ArgumentError("the argument for hours to be subtracted must be a number.")
        return DateTimeMetaclass.createFromLocalDateTime(self.jDateTime.minusHours(hours.toLong()))
    }

    private fun subtractMinutesDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxDateTime{
        val self = interpreter.thisInstance as LoxDateTime
        val minutes = arguments!![0] as? Double ?: throw ArgumentError("the argument for minutes to be subtracted must be a number.")
        return DateTimeMetaclass.createFromLocalDateTime(self.jDateTime.minusMinutes(minutes.toLong()))
    }

    private fun subtractSecondsDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxDateTime{
        val self = interpreter.thisInstance as LoxDateTime
        val seconds = arguments!![0] as? Double ?: throw ArgumentError("the argument for seconds to be subtracted must be a number.")
        return DateTimeMetaclass.createFromLocalDateTime(self.jDateTime.minusSeconds(seconds.toLong()))
    }

    private fun textFromDuration(duration: LoxObject) : String{
        val days = duration.getProperty("days") as Double
        val hours = duration.getProperty("hours") as Double
        val minutes = duration.getProperty("minutes") as Double
        val seconds = duration.getProperty("seconds") as Double

        var text = "P"
        if(days > 0) text += "${days.toInt()}D"
        if(hours == 0.0 && minutes == 0.0 && seconds == 0.0) return text

        text += "T"
        if(hours > 0) text += "${hours.toInt()}H"
        if(minutes > 0) text += "${minutes.toInt()}M"
        if(seconds > 0) text += "${seconds.toInt()}S"
        return text
    }

    private fun timestampProp(interpreter: Interpreter, arguments: List<Any?>?) : Long{
        val self = interpreter.thisInstance as LoxDateTime
        val timestamp = Timestamp.valueOf(self.jDateTime)
        return timestamp.time
    }

    private fun toDateDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxDate{
        val self = interpreter.thisInstance as LoxDateTime
        return LoxDate(self.jDateTime.toLocalDate())
    }

    private fun toStringDef(interpreter: Interpreter, arguments: List<Any?>?) = (interpreter.thisInstance as LoxDateTime).toString()
}