package com.mysidia.ktlox.common

import com.mysidia.ktlox.std.util.DateTimeClass
import java.time.LocalDateTime

class LoxDateTime(klass: LoxClass = DateTimeClass) : LoxObject(klass){

    lateinit var jDateTime : LocalDateTime

    override fun toString(): String {
        return "${jDateTime.year}-${jDateTime.monthValue}-${jDateTime.dayOfMonth} ${jDateTime.hour}:${jDateTime.minute}:${jDateTime.second}"
    }
}