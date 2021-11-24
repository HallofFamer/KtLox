package com.mysidia.ktlox.std.lang

import com.mysidia.ktlox.common.*
import com.mysidia.ktlox.interpreter.ArgumentError
import com.mysidia.ktlox.interpreter.Interpreter
import kotlin.math.*

object NumberClass : LoxNativeClass("Number", ObjectClass){

    init{
        defineNativeMetaclass("Number class")
        defineNativeMethod("abs", 0, this::absDef)
        defineNativeMethod("ceil", 0, this::ceilDef)
        defineNativeMethod("exp", 0, this::expDef)
        defineNativeMethod("floor", 0, this::floorDef)
        defineNativeMethod("hypot", 1, this::hypotDef)
        defineNativeMethod("init", 1, this::initDef)
        defineNativeMethod("isFloat", 0, this::isFloatDef)
        defineNativeMethod("isInt", 0, this::isIntDef)
        defineNativeMethod("isNumber", 0, this::isNumberDef)
        defineNativeMethod("ln", 0, this::lnDef)
        defineNativeMethod("log", 1, this::logDef)
        defineNativeMethod("max", 1, this::maxDef)
        defineNativeMethod("min", 1, this::minDef)
        defineNativeMethod("pow", 1, this::powDef)
        defineNativeMethod("round", 1, this::roundDef)
        defineNativeMethod("sqrt", 0, this::sqrtDef)
        defineNativeMethod("squared", 0, this::squaredDef)
        defineNativeMethod("step", 3, this::stepDef)
        defineNativeMethod("toFloat", 0, this::toFloatDef)
        defineNativeMethod("toInt", 0, this::toIntDef)
        defineNativeMethod("toString", 0, this::toStringDef)
    }

    private fun absDef(interpreter: Interpreter, arguments: List<Any?>?) = abs(LoxNumber.value.toDouble())

    private fun ceilDef(interpreter: Interpreter, arguments: List<Any?>?) = ceil(LoxNumber.value.toDouble())

    private fun expDef(interpreter: Interpreter, arguments: List<Any?>?) = exp(LoxNumber.value.toDouble())

    private fun floorDef(interpreter: Interpreter, arguments: List<Any?>?) = floor(LoxNumber.value.toDouble())

    private fun hypotDef(interpreter: Interpreter, arguments: List<Any?>?) : Double{
        val self = LoxNumber.value
        val other = arguments!![0] as? Number ?: throw ArgumentError("the first argument for hypot must be a number.")
        return hypot(self.toDouble(), other.toDouble())
    }

    override fun initDef(interpreter: Interpreter, arguments: List<Any?>?) : Any{
        throw ArgumentError("Cannot create instance from abstract class Number.")
    }

    private fun isFloatDef(interpreter: Interpreter, arguments: List<Any?>?) = false

    private fun isIntDef(interpreter: Interpreter, arguments: List<Any?>?) = false

    private fun isNumberDef(interpreter: Interpreter, arguments: List<Any?>?) = true

    private fun lnDef(interpreter: Interpreter, arguments: List<Any?>?) = ln(LoxNumber.value.toDouble())

    private fun logDef(interpreter: Interpreter, arguments: List<Any?>?) : Double{
        val self = LoxNumber.value
        val base = arguments!![0] as? Number ?: throw ArgumentError("the first argument for log must be a number.")
        return log(self.toDouble(), base.toDouble())
    }

    private fun maxDef(interpreter: Interpreter, arguments: List<Any?>?) : Number{
        val self = LoxNumber.value
        val other = arguments!![0] as? Number ?: throw ArgumentError("the first argument for max must be a number.")
        return if(self.toDouble() >= other.toDouble()) self else other
    }

    private fun minDef(interpreter: Interpreter, arguments: List<Any?>?) : Number{
        val self = LoxNumber.value
        val other = arguments!![0] as? Number ?: throw ArgumentError("the first argument for min must be a number.")
        return if(self.toDouble() <= other.toDouble()) self else other
    }

    private fun powDef(interpreter: Interpreter, arguments: List<Any?>?) : Number{
        val self = LoxNumber.value
        val exponent = arguments!![0] as? Number ?: throw ArgumentError("the first argument for pow must be a number.")
        return self.toDouble().pow(exponent.toDouble())
    }

    private fun roundDef(interpreter: Interpreter, arguments: List<Any?>?) : Double{
        val self = LoxNumber.value
        val numDigits = arguments!![0] as? Number ?: throw ArgumentError("the first argument for roundDef must be a number.")
        val factor = 10.0.pow(numDigits.toDouble())
        return (self.toDouble() * factor).roundToInt() / factor
    }

    private fun sqrtDef(interpreter: Interpreter, arguments: List<Any?>?) = sqrt(LoxNumber.value.toDouble())

    private fun squaredDef(interpreter: Interpreter, arguments: List<Any?>?) = LoxNumber.value.toDouble().pow(2)

    private fun stepDef(interpreter: Interpreter, arguments: List<Any?>?) {
        TODO("To be implemented after short closure/lambda and nonlocal return features are available")
    }

    private fun toFloatDef(interpreter: Interpreter, arguments: List<Any?>?) = LoxNumber.value.toDouble()

    private fun toIntDef(interpreter: Interpreter, arguments: List<Any?>?) = LoxNumber.value.toLong()

    private fun toStringDef(interpreter: Interpreter, arguments: List<Any?>?) = LoxNumber.value.toString()
}