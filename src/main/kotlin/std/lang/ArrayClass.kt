package com.mysidia.ktlox.std.lang

import com.mysidia.ktlox.common.*
import com.mysidia.ktlox.interpreter.ArgumentError
import com.mysidia.ktlox.interpreter.Interpreter
import com.mysidia.ktlox.std.collection.LoxList

object ArrayClass : LoxNativeClass("Array", ObjectClass, null, ArrayMetaclass){

    init{
        defineNativeGetter("length", this::lengthProp)
        defineNativeMethod("contains", 1, this::containsDef)
        defineNativeMethod("copy", 0, this::copyDef)
        defineNativeMethod("copyFrom", 2, this::copyFromDef)
        defineNativeMethod("get", 1, this::getDef)
        defineNativeMethod("indexOf", 1, this::indexOfDef)
        defineNativeMethod("init", 1, this::initDef)
        defineNativeMethod("lastIndexOf", 1, this::lastIndexOfDef)
        defineNativeMethod("set", 2, this::setDef)
        defineNativeMethod("toList", 0, this::toListDef)
        defineNativeMethod("toString", 0, this::toStringDef)
    }

    override val arity = 1

    override val isNative = true

    override fun initDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxArray{
        val thisInstance = interpreter.thisInstance as? LoxArray ?: LoxArray()
        val length = arguments!![0] as? Long ?: throw ArgumentError("array size argument must be an integer.")
        thisInstance.length = length.toInt()
        thisInstance.elements = arrayOfNulls(thisInstance.length)
        return thisInstance
    }

    override fun new(klass: LoxClass) = LoxArray(klass)

    private fun containsDef(interpreter: Interpreter, arguments: List<Any?>?) : Boolean{
        val self = interpreter.thisInstance as LoxArray
        val element = arguments!![0]
        return self.elements.contains(element)
    }

    private fun copyDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxArray{
        val self = interpreter.thisInstance as LoxArray
        val other = LoxArray(self.length, self.klass!!)
        other.elements = self.elements.copyOf(self.length)
        return other
    }

    private fun copyFromDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxArray{
        val self = interpreter.thisInstance as LoxArray
        val startIndex = arguments!![0] as? Long ?: throw ArgumentError("array starting index must be an integer.")
        val endIndex = arguments[1] as? Long ?: throw ArgumentError("array ending index must be an integer.")
        if(endIndex <= startIndex) throw ArgumentError("The ending index must be greater than the starting index.")
        val length = endIndex - startIndex
        val other = LoxArray(length.toInt(), self.klass!!)
        other.elements = self.elements.copyOfRange(startIndex.toInt(), endIndex.toInt())
        return other
    }

    private fun getDef(interpreter: Interpreter, arguments: List<Any?>?) : Any?{
        val self = interpreter.thisInstance as LoxArray
        val index = arguments!![0] as? Long ?: throw ArgumentError("array index argument must be an integer.")
        return self.elements[index.toInt()]
    }

    private fun indexOfDef(interpreter: Interpreter, arguments: List<Any?>?) : Long{
        val self = interpreter.thisInstance as LoxArray
        val element = arguments!![0]
        return self.elements.indexOf(element).toLong()
    }

    private fun lastIndexOfDef(interpreter: Interpreter, arguments: List<Any?>?) : Long{
        val self = interpreter.thisInstance as LoxArray
        val element = arguments!![0]
        return self.elements.lastIndexOf(element).toLong()
    }

    private fun lengthProp(interpreter: Interpreter, arguments: List<Any?>?) = (interpreter.thisInstance as LoxArray).length.toLong()

    private fun setDef(interpreter: Interpreter, arguments: List<Any?>?){
        val self = interpreter.thisInstance as LoxArray
        val index = arguments!![0] as? Long ?: throw ArgumentError("array index argument must be an integer.")
        val elementIndex = index.toInt()
        if(elementIndex >= self.length) throw ArgumentError("Supplied index argument is invalid, it exceeds the array length.")
        self.elements[elementIndex] = arguments[1]
    }

    private fun toListDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxList {
        val self = interpreter.thisInstance as LoxArray
        return LoxList(self.elements.toMutableList())
    }

    private fun toStringDef(interpreter: Interpreter, arguments: List<Any?>?) = (interpreter.thisInstance as LoxArray).toString()
}