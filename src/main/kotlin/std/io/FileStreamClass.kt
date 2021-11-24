package com.mysidia.ktlox.std.io

import com.mysidia.ktlox.common.*
import com.mysidia.ktlox.interpreter.ArgumentError
import com.mysidia.ktlox.interpreter.Interpreter
import com.mysidia.ktlox.std.lang.ObjectClass
import java.io.File

object FileStreamClass : LoxNativeClass("FileStream", ObjectClass){

    init{
        defineNativeMetaclass("FileStream class")
        defineNativeGetter("position", this::positionProp)
        defineNativeGetter("readonly", this::readonlyProp)
        defineNativeMethod("append", 1, this::appendDef)
        defineNativeMethod("appendLine", 0, this::appendLineDef)
        defineNativeMethod("appendString", 1, this::appendStringDef)
        defineNativeMethod("close", 0, this::closeDef)
        defineNativeMethod("flush", 0, this::flushDef)
        defineNativeMethod("init", 2, this::initDef)
        defineNativeMethod("next", 0, this::nextDef)
        defineNativeMethod("nextLine", 0, this::nextLineDef)
        defineNativeMethod("peek", 0, this::peekDef)
        defineNativeMethod("put", 1, this::putDef)
        defineNativeMethod("putString", 1, this::putStringDef)
        defineNativeMethod("reset", 0, this::resetDef)
        defineNativeMethod("setPosition", 1, this::setPositionDef)
        defineNativeMethod("skip", 1, this::skipDef)
    }

    override val arity = 2

    override val isNative = true

    override fun initDef(interpreter: Interpreter, arguments: List<Any?>?): LoxFileStream {
        val thisInstance = interpreter.thisInstance as? LoxFileStream ?: LoxFileStream()
        val file = arguments!![0] as? LoxFile ?: LoxFile(File(arguments[0].toString()))
        val readOnly = arguments[1] as Boolean
        if(!file.jFile.isFile) throw ArgumentError("The supplied file name is invalid")
        thisInstance.jReader = file.jFile.bufferedReader()
        thisInstance.jReader.mark(0)
        if(!readOnly) thisInstance.jWriter = file.jFile.bufferedWriter()
        return thisInstance
    }

    override fun new(klass: LoxClass) = LoxFileStream(klass)

    override fun thisInstance(interpreter: Interpreter) = interpreter.thisInstance as LoxFileStream

    private fun appendDef(interpreter: Interpreter, arguments: List<Any?>?){
        val self = thisInstance(interpreter)
        if(self.readOnly) throw ArgumentError("Cannot write to the stream, it is readonly.")
        val char = arguments!![0] as? String ?: " "
        self.jWriter!!.append(char[0])
    }

    private fun appendLineDef(interpreter: Interpreter, arguments: List<Any?>?){
        val self = thisInstance(interpreter)
        if(self.readOnly) throw ArgumentError("Cannot write to the stream, it is readonly.")
        self.jWriter!!.appendLine()
    }

    private fun appendStringDef(interpreter: Interpreter, arguments: List<Any?>?){
        val self = thisInstance(interpreter)
        if(self.readOnly) throw ArgumentError("Cannot write to the stream, it is readonly.")
        val string = arguments!![0] as? String ?: " "
        self.jWriter!!.append(string)
    }

    private fun closeDef(interpreter: Interpreter, arguments: List<Any?>?) = thisInstance(interpreter).close()

    private fun flushDef(interpreter: Interpreter, arguments: List<Any?>?) = thisInstance(interpreter).jWriter!!.flush()

    private fun nextDef(interpreter: Interpreter, arguments: List<Any?>?) : String{
        val self = thisInstance(interpreter)
        self.position++
        return self.jReader.read().toChar().toString()
    }

    private fun nextLineDef(interpreter: Interpreter, arguments: List<Any?>?) : String?{
        val self = thisInstance(interpreter)
        val text = self.jReader.readLine()
        self.position += text.length
        return text
    }

    private fun peekDef(interpreter: Interpreter, arguments: List<Any?>?) : String{
        val self = thisInstance(interpreter)
        val current = self.position.toInt()
        self.jReader.mark(current)
        val char = self.jReader.read()
        self.jReader.reset()
        self.jReader.mark(0)
        return char.toChar().toString()
    }

    private fun positionProp(interpreter: Interpreter, arguments: List<Any?>?) = thisInstance(interpreter).position

    private fun putDef(interpreter: Interpreter, arguments: List<Any?>?){
        val self = thisInstance(interpreter)
        if(self.readOnly) throw ArgumentError("Cannot write to the stream, it is readonly.")
        val char = arguments!![0] as? String ?: " "
        self.jWriter!!.write(char[0].code)
    }

    private fun putStringDef(interpreter: Interpreter, arguments: List<Any?>?){
        val self = thisInstance(interpreter)
        if(self.readOnly) throw ArgumentError("Cannot write to the stream, it is readonly.")
        val string = arguments!![0] as? String ?: " "
        self.jWriter!!.write(string)
    }

    private fun readonlyProp(interpreter: Interpreter, arguments: List<Any?>?) = thisInstance(interpreter).readOnly

    private fun resetDef(interpreter: Interpreter, arguments: List<Any?>?) = thisInstance(interpreter).jReader.reset()

    private fun setPositionDef(interpreter: Interpreter, arguments: List<Any?>?){
        val self = thisInstance(interpreter)
        val position = arguments!![0] as? Long ?: throw ArgumentError("The position(first argument) must be an integer.")
        self.jReader.reset()
        self.jReader.skip(position)
        self.position = position
    }

    private fun skipDef(interpreter: Interpreter, arguments: List<Any?>?) : Long{
        val self = thisInstance(interpreter)
        val numChars = arguments!![0] as? Long ?: throw ArgumentError("The number of characters to skip(first argument) must be an integer.")
        val numSkipped = self.jReader.skip(numChars)
        self.position += numSkipped
        return numSkipped
    }
}