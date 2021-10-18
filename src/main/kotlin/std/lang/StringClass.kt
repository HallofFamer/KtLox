package com.mysidia.ktlox.std.lang

import com.mysidia.ktlox.common.*
import com.mysidia.ktlox.interpreter.ArgumentError
import com.mysidia.ktlox.interpreter.Interpreter
import java.util.*

object StringClass : LoxNativeClass("String", ObjectClass) {

    init{
        defineNativeMetaclass("String class")
        defineNativeGetter("length", this::lengthProp)
        defineNativeMethod("capitalize", 0, this::capitalizeDef)
        defineNativeMethod("contains", 1, this::containsDef)
        //defineNativeMethod("containsAll", 1, this::containsAllDef)
        //defineNativeMethod("containsAnyDef", 1, this::containsAnyDef)
        defineNativeMethod("decapitalize", 0, this::decapitalizeDef)
        defineNativeMethod("endsWith", 1, this::endsWithDef)
        defineNativeMethod("indexOf", 1, this::indexOfDef)
        defineNativeMethod("isEmpty", 0, this::isEmptyDef)
        defineNativeMethod("isLowercase", 0, this::isLowercaseDef)
        defineNativeMethod("isNilOrEmpty", 0, this::isNilOrEmptyDef)
        defineNativeMethod("isString", 0, this::isStringDef)
        defineNativeMethod("isUnicase", 0, this::isUnicaseDef)
        defineNativeMethod("isUppercase", 0, this::isUppercaseDef)
        defineNativeMethod("lastIndexOf", 1, this::lastIndexOfDef)
        defineNativeMethod("matches", 1, this::matchesDef)
        defineNativeMethod("padEnd", 2, this::padEndDef)
        defineNativeMethod("padStart", 2, this::padStartDef)
        defineNativeMethod("remove", 1, this::removeDef)
        //defineNativeMethod("removeAll", 1, this::removeAllDef)
        defineNativeMethod("replace", 2, this::replaceDef)
        //defineNativeMethod("replaceAll", 2, this::replaceAllDef)
        defineNativeMethod("reverse", 0, this::reverseDef)
        //defineNativeMethod("split", 1, this::splitDef)
        defineNativeMethod("startsWith",  1, this::startsWithDef)
        defineNativeMethod("substring",2, this::substringDef)
        defineNativeMethod("substringAfterFirst", 1, this::substringAfterFirstDef)
        defineNativeMethod("substringAfterLast", 1, this::substringAfterLastDef)
        defineNativeMethod("substringBeforeFirst", 1, this::substringBeforeFirstDef)
        defineNativeMethod("substringBeforeLast", 1, this::substringBeforeLastDef)
        //defineNativeMethod("toCharArray", 0, this::toCharArrayDef)
        defineNativeMethod("toLowercase", 0, this::toLowercaseDef)
        defineNativeMethod("toString", 0, this::toStringDef)
        defineNativeMethod("toUppercase", 0, this::toUppercaseDef)
        defineNativeMethod("trim", 0, this::trimDef)
        defineNativeMethod("trimEnd", 0, this::trimEndDef)
        defineNativeMethod("trimStart", 0, this::trimStartDef)
    }

    override val arity = 1

    override fun call(interpreter: Interpreter, arguments: List<Any?>?) = arguments!![0] as? String ?: ""

    private fun capitalizeDef(interpreter: Interpreter, arguments: List<Any?>?) : String{
        val self = LoxString.value
        return self.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }

    private fun containsDef(interpreter: Interpreter, arguments: List<Any?>?) = LoxString.value.contains(arguments!![0] as String)

    private fun decapitalizeDef(interpreter: Interpreter, arguments: List<Any?>?) : String{
        val self = LoxString.value
        return self.replaceFirstChar { it.lowercase(Locale.getDefault()) }
    }

    private fun endsWithDef(interpreter: Interpreter, arguments: List<Any?>?) = LoxString.value.endsWith(arguments!![0] as String)

    private fun indexOfDef(interpreter: Interpreter, arguments: List<Any?>?) = LoxString.value.indexOf(arguments!![0] as String)

    private fun isEmptyDef(interpreter: Interpreter, arguments: List<Any?>?) = LoxString.length == 0

    private fun isLowercaseDef(interpreter: Interpreter, arguments: List<Any?>?) : Boolean{
        val self = LoxString.value
        return (self == self.lowercase(Locale.getDefault()))
    }

    private fun isUnicaseDef(interpreter: Interpreter, arguments: List<Any?>?) : Boolean{
        val self = LoxString.value
        return (self == self.lowercase(Locale.getDefault()) || self == self.uppercase(Locale.getDefault()))
    }

    private fun isUppercaseDef(interpreter: Interpreter, arguments: List<Any?>?) : Boolean{
        val self = LoxString.value
        return (self == self.uppercase(Locale.getDefault()))
    }

    private fun isNilOrEmptyDef(interpreter: Interpreter, arguments: List<Any?>?) = isEmptyDef(interpreter, arguments)

    private fun isStringDef(interpreter: Interpreter, arguments: List<Any?>?) = true

    private fun lastIndexOfDef(interpreter: Interpreter, arguments: List<Any?>?) = LoxString.value.lastIndexOf(arguments!![0] as String)

    private fun lengthProp(interpreter: Interpreter, arguments: List<Any?>?) = LoxString.length

    private fun matchesDef(interpreter: Interpreter, arguments: List<Any?>?) = LoxString.value.matches(Regex(arguments!![0] as String))

    private fun padEndDef(interpreter: Interpreter, arguments: List<Any?>?) : String{
        val self = LoxString.value
        val length = arguments!![0] as? Double ?: throw ArgumentError("the first argument must be a number")
        val padChar = arguments[1] as String
        return self.padEnd(length.toInt(), padChar[0])
    }

    private fun padStartDef(interpreter: Interpreter, arguments: List<Any?>?) : String{
        val self = LoxString.value
        val length = arguments!![0] as? Double ?: throw ArgumentError("the first argument must be a number")
        val padChar = arguments[1] as String
        return self.padStart(length.toInt(), padChar[0])
    }

    private fun removeDef(interpreter: Interpreter, arguments: List<Any?>?) : String{
        val self = LoxString.value
        val value = arguments!![0] as String
        return self.replace(value, "", false)
    }

    private fun replaceDef(interpreter: Interpreter, arguments: List<Any?>?) : String{
        val self = LoxString.value
        val oldValue = arguments!![0] as String
        val newValue = arguments[1] as String
        return self.replace(oldValue, newValue, false)
    }

    private fun reverseDef(interpreter: Interpreter, arguments: List<Any?>?) = LoxString.value.reversed()

    private fun startsWithDef(interpreter: Interpreter, arguments: List<Any?>?) = LoxString.value.startsWith(arguments!![0] as String)

    private fun substringDef(interpreter: Interpreter, arguments: List<Any?>?) : String{
        val self = LoxString.value
        val start = arguments!![0] as? Double ?: throw ArgumentError("the start index(first argument) must be a number.")
        val count = arguments[1] as? Double ?: throw ArgumentError("the length(second argument) must be a number.")
        return self.substring(start.toInt(), (start + count).toInt())
    }

    private fun substringAfterFirstDef(interpreter: Interpreter, arguments: List<Any?>?) = LoxString.value.substringAfter(arguments!![0] as String)

    private fun substringAfterLastDef(interpreter: Interpreter, arguments: List<Any?>?) = LoxString.value.substringAfterLast(arguments!![0] as String)

    private fun substringBeforeFirstDef(interpreter: Interpreter, arguments: List<Any?>?) = LoxString.value.substringBefore(arguments!![0] as String)

    private fun substringBeforeLastDef(interpreter: Interpreter, arguments: List<Any?>?) = LoxString.value.substringBeforeLast(arguments!![0] as String)

    private fun toLowercaseDef(interpreter: Interpreter, arguments: List<Any?>?) = LoxString.value.lowercase(Locale.getDefault())

    private fun toStringDef(interpreter: Interpreter, arguments: List<Any?>?) = LoxString.value

    private fun toUppercaseDef(interpreter: Interpreter, arguments: List<Any?>?) = LoxString.value.uppercase(Locale.getDefault())

    private fun trimDef(interpreter: Interpreter, arguments: List<Any?>?) = LoxString.value.trim()

    private fun trimEndDef(interpreter: Interpreter, arguments: List<Any?>?) = LoxString.value.trimEnd()

    private fun trimStartDef(interpreter: Interpreter, arguments: List<Any?>?) = LoxString.value.trimStart()
}