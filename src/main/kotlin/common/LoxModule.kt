package com.mysidia.ktlox.common

import com.mysidia.ktlox.interpreter.Environment

interface LoxModule{
    fun registerModule(env: Environment)
}