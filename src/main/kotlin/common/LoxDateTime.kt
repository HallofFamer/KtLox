package com.mysidia.ktlox.common

import com.mysidia.ktlox.std.lang.DateTimeClass
import java.time.LocalDateTime

class LoxDateTime(val jDateTime: LocalDateTime) : LoxObject(DateTimeClass){

    override fun toString(): String {
        return "${jDateTime.year}-${jDateTime.monthValue}-${jDateTime.dayOfMonth} ${jDateTime.hour}:${jDateTime.minute}:${jDateTime.second}"
    }
}