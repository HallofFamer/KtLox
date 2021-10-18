package com.mysidia.ktlox.common

import com.mysidia.ktlox.std.lang.DateClass
import java.time.LocalDate

class LoxDate(val jDate: LocalDate) : LoxObject(DateClass){

    override fun toString(): String {
        return "${jDate.year}-${jDate.monthValue}-${jDate.dayOfMonth}"
    }
}