package com.mysidia.ktlox.std.lang

import com.mysidia.ktlox.common.*
import com.mysidia.ktlox.interpreter.ArgumentError
import com.mysidia.ktlox.interpreter.Interpreter
import kotlin.math.*

object IntClass : LoxNativeClass("Int", NumberClass){

    init{
        defineNativeMetaclass("Int class")
        defineNativeMethod("abs", 0, this::absDef)
        defineNativeMethod("downto", 2, this::downtoDef)
        defineNativeMethod("even", 0, this::evenDef)
        defineNativeMethod("factorial", 0, this::factorialDef)
        defineNativeMethod("gcd", 1, this::gcdDef)
        defineNativeMethod("init", 1, this::initDef)
        defineNativeMethod("isInt", 0, this::isIntDef)
        defineNativeMethod("lcm", 1, this::lcmDef)
        defineNativeMethod("odd", 0, this::oddDef)
        defineNativeMethod("timesRepeat", 1, this::timesRepeatDef)
        defineNativeMethod("upto", 2, this::uptoDef)
    }

    override val arity = 1

    override fun initDef(interpreter: Interpreter, arguments: List<Any?>?) = arguments!![0] as? Long ?: 0L

    private fun absDef(interpreter: Interpreter, arguments: List<Any?>?) = abs(LoxInt.value)

    private fun downtoDef(interpreter: Interpreter, arguments: List<Any?>?) {
        TODO("To be implemented after short closure/lambda and nonlocal return features are available")
    }

    private fun evenDef(interpreter: Interpreter, arguments: List<Any?>?) = LoxInt.value % 2 == 0L

    private fun factorialDef(interpreter: Interpreter, arguments: List<Any?>?) : Long{
        val self = LoxInt.value
        if(self < 0L) throw ArgumentError("Cannot compute factorial on negative integers.")
        var i = 1
        var product = 1L
        while(i <= self){
            product *= i
            i++
        }
        return product
    }

    private fun gcdDef(interpreter: Interpreter, arguments: List<Any?>?) : Long{
        val other = arguments!![0] as? Long ?: throw ArgumentError("the first argument for gcd must be an integer.")
        return LoxInt.gcd(other)
    }

    private fun isIntDef(interpreter: Interpreter, arguments: List<Any?>?) = true

    private fun lcmDef(interpreter: Interpreter, arguments: List<Any?>?) : Long{
        val other = arguments!![0] as? Long ?: throw ArgumentError("the first argument for lcm must be an integer.")
        return LoxInt.value * other / LoxInt.gcd(other)
    }

    private fun oddDef(interpreter: Interpreter, arguments: List<Any?>?) = LoxInt.value % 2 == 1L

    private fun timesRepeatDef(interpreter: Interpreter, arguments: List<Any?>?) {
        TODO("To be implemented after short closure/lambda and nonlocal return features are available")
    }

    private fun uptoDef(interpreter: Interpreter, arguments: List<Any?>?) {
        TODO("To be implemented after short closure/lambda and nonlocal return features are available")
    }
}