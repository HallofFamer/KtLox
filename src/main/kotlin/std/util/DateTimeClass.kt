package com.mysidia.ktlox.std.util

import com.mysidia.ktlox.common.*
import com.mysidia.ktlox.interpreter.ArgumentError
import com.mysidia.ktlox.interpreter.Interpreter
import com.mysidia.ktlox.std.lang.ObjectClass
import java.sql.Timestamp
import java.time.DateTimeException
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object DateTimeClass : LoxNativeClass("DateTime", ObjectClass, null, DateTimeMetaclass){

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

    override val arity = 6

    override val isNative = true

    override fun initDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxDateTime{
        try{
            val thisInstance = interpreter.thisInstance as? LoxDateTime
            val year = arguments!![0] as? Long ?: throw ArgumentError("the argument for year must be an integer.")
            val month = arguments[1] as? Long ?: throw ArgumentError("the argument for month must be an integer.")
            val day = arguments[2] as? Long ?: throw ArgumentError("the argument for day must be an integer.")
            val hour = arguments[3] as? Long ?: throw ArgumentError("the argument for hour must be an integer.")
            val minute = arguments[4] as? Long ?: throw ArgumentError("the argument for minute must be an integer.")
            val second = arguments[5] as? Long ?: throw ArgumentError("the argument for second must be an integer.")
            return DateTimeMetaclass.createFromLocalDateTime(
                LocalDateTime.of(
                    year.toInt(),
                    month.toInt(),
                    day.toInt(),
                    hour.toInt(),
                    minute.toInt(),
                    second.toInt()
                ), thisInstance
            )
        }
        catch(ex: DateTimeException){
            throw ArgumentError(ex.message!!)
        }
    }

    override fun new(klass: LoxClass) = LoxDateTime(klass)

    override fun thisInstance(interpreter: Interpreter) = interpreter.thisInstance as LoxDateTime

    private fun addDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxDateTime{
        val self = thisInstance(interpreter)
        val text = textFromDuration(arguments!![0] as LoxObject)
        return DateTimeMetaclass.createFromLocalDateTime(self.jDateTime.plus(Duration.parse(text)))
    }

    private fun addDaysDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxDateTime{
        val self = thisInstance(interpreter)
        val days = arguments!![0] as? Long ?: throw ArgumentError("the argument for days to be added must be an integer.")
        return DateTimeMetaclass.createFromLocalDateTime(self.jDateTime.plusDays(days))
    }

    private fun addHoursDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxDateTime{
        val self = thisInstance(interpreter)
        val hours = arguments!![0] as? Long ?: throw ArgumentError("the argument for hours to be added must be an integer.")
        return DateTimeMetaclass.createFromLocalDateTime(self.jDateTime.plusHours(hours))
    }

    private fun addMinutesDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxDateTime{
        val self = thisInstance(interpreter)
        val minutes = arguments!![0] as? Long ?: throw ArgumentError("the argument for minutes to be added must be an integer.")
        return DateTimeMetaclass.createFromLocalDateTime(self.jDateTime.plusMinutes(minutes))
    }

    private fun addSecondsDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxDateTime{
        val self = thisInstance(interpreter)
        val seconds = arguments!![0] as? Long ?: throw ArgumentError("the argument for seconds to be added must be an integer.")
        return DateTimeMetaclass.createFromLocalDateTime(self.jDateTime.plusSeconds(seconds))
    }

    private fun diffDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxObject{
        val self = thisInstance(interpreter)
        val other = arguments!![0] as? LoxDateTime ?: throw ArgumentError("The argument must be an instance of DateTime")
        return DurationMetaclass.createFromTimeDiff(self, other)
    }

    private fun diffTimeDef(interpreter: Interpreter, arguments: List<Any?>?) : Long{
        val self = thisInstance(interpreter)
        val other = arguments!![0] as? LoxDateTime ?: throw ArgumentError("The argument must be an instance of DateTime")
        return self.jDateTime.until(other.jDateTime, ChronoUnit.SECONDS)
    }

    private fun formatDef(interpreter: Interpreter, arguments: List<Any?>?) : String{
        try {
            val self = thisInstance(interpreter)
            val format = arguments!![0] as? String ?: throw ArgumentError("format argument must be a string!")
            return self.jDateTime.format(DateTimeFormatter.ofPattern(format))
        }
        catch(ex: DateTimeException){
            throw ArgumentError(ex.message!!)
        }
    }

    private fun subtractDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxDateTime{
        val self = thisInstance(interpreter)
        val text = textFromDuration(arguments!![0] as LoxObject)
        return DateTimeMetaclass.createFromLocalDateTime(self.jDateTime.minus(Duration.parse(text)))
    }

    private fun subtractDaysDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxDateTime{
        val self = thisInstance(interpreter)
        val days = arguments!![0] as? Long ?: throw ArgumentError("the argument for days to be subtracted must be an integer.")
        return DateTimeMetaclass.createFromLocalDateTime(self.jDateTime.minusDays(days))
    }

    private fun subtractHoursDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxDateTime{
        val self = thisInstance(interpreter)
        val hours = arguments!![0] as? Long ?: throw ArgumentError("the argument for hours to be subtracted must be an integer.")
        return DateTimeMetaclass.createFromLocalDateTime(self.jDateTime.minusHours(hours))
    }

    private fun subtractMinutesDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxDateTime{
        val self = thisInstance(interpreter)
        val minutes = arguments!![0] as? Long ?: throw ArgumentError("the argument for minutes to be subtracted must be an integer.")
        return DateTimeMetaclass.createFromLocalDateTime(self.jDateTime.minusMinutes(minutes))
    }

    private fun subtractSecondsDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxDateTime{
        val self = thisInstance(interpreter)
        val seconds = arguments!![0] as? Long ?: throw ArgumentError("the argument for seconds to be subtracted must be an integer.")
        return DateTimeMetaclass.createFromLocalDateTime(self.jDateTime.minusSeconds(seconds))
    }

    private fun textFromDuration(duration: LoxObject) : String{
        val days = duration.getProperty("days") as Long
        val hours = duration.getProperty("hours") as Long
        val minutes = duration.getProperty("minutes") as Long
        val seconds = duration.getProperty("seconds") as Long

        var text = "P"
        if(days > 0) text += "${days.toInt()}D"
        if(hours == 0L && minutes == 0L && seconds == 0L) return text

        text += "T"
        if(hours > 0) text += "${hours.toInt()}H"
        if(minutes > 0) text += "${minutes.toInt()}M"
        if(seconds > 0) text += "${seconds.toInt()}S"
        return text
    }

    private fun timestampProp(interpreter: Interpreter, arguments: List<Any?>?) : Long{
        val self = thisInstance(interpreter)
        val timestamp = Timestamp.valueOf(self.jDateTime)
        return timestamp.time
    }

    private fun toDateDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxDate{
        val self = thisInstance(interpreter)
        val date = LoxDate()
        date.jDate = self.jDateTime.toLocalDate()
        return date
    }

    private fun toStringDef(interpreter: Interpreter, arguments: List<Any?>?) = thisInstance(interpreter).toString()
}