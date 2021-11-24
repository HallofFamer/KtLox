package com.mysidia.ktlox.common

import com.mysidia.ktlox.std.util.DateClass
import java.time.LocalDate

class LoxDate(klass: LoxClass = DateClass) : LoxObject(klass){

    lateinit var jDate : LocalDate

    override fun toString() = "${jDate.year}-${jDate.monthValue}-${jDate.dayOfMonth}"
}