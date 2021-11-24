package com.mysidia.ktlox.std.io

import com.mysidia.ktlox.common.*
import com.mysidia.ktlox.interpreter.ArgumentError
import com.mysidia.ktlox.interpreter.Interpreter
import com.mysidia.ktlox.std.lang.ObjectClass
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File

object BinaryStreamClass : LoxNativeClass("BinaryStream", ObjectClass){

    init{
        defineNativeMetaclass("BinaryStream class")
        defineNativeGetter("position", this::positionProp)
        defineNativeGetter("readonly", this::readonlyProp)
        defineNativeMethod("close", 0, this::closeDef)
        defineNativeMethod("flush", 0, this::flushDef)
        defineNativeMethod("init", 2, this::initDef)
        defineNativeMethod("next", 0, this::nextDef)
        defineNativeMethod("peek", 1, this::peekDef)
        defineNativeMethod("put", 1, this::putDef)
        defineNativeMethod("reset", 0, this::resetDef)
        defineNativeMethod("setPosition", 1, this::setPositionDef)
        defineNativeMethod("skip", 1, this::skipDef)
    }

    override val arity = 2

    override val isNative = true

    override fun initDef(interpreter: Interpreter, arguments: List<Any?>?): LoxBinaryStream {
        val thisInstance = interpreter.thisInstance as? LoxBinaryStream ?: LoxBinaryStream()
        val file = arguments!![0] as? LoxFile ?: LoxFile(File(arguments[0].toString()))
        val readOnly = arguments[1] as Boolean
        if(!file.jFile.isFile) throw ArgumentError("The supplied file name is invalid")
        thisInstance.jReader = BufferedInputStream(file.jFile.inputStream())
        thisInstance.jReader.mark(0)
        if(!readOnly) thisInstance.jWriter = BufferedOutputStream(file.jFile.outputStream())
        return thisInstance
    }

    override fun new(klass: LoxClass) = LoxBinaryStream(klass)

    override fun thisInstance(interpreter: Interpreter) = interpreter.thisInstance as LoxBinaryStream

    private fun closeDef(interpreter: Interpreter, arguments: List<Any?>?) = thisInstance(interpreter).close()

    private fun flushDef(interpreter: Interpreter, arguments: List<Any?>?){
        val self = thisInstance(interpreter)
        if(self.readOnly) throw ArgumentError("Cannot flush the stream, it is readonly.")
        self.jWriter!!.flush()
    }

    private fun nextDef(interpreter: Interpreter, arguments: List<Any?>?) : Long{
        val self = thisInstance(interpreter)
        val byte = self.jReader.read()
        self.position++
        return byte.toLong()
    }

    private fun peekDef(interpreter: Interpreter, arguments: List<Any?>?) : Long{
        val self = thisInstance(interpreter)
        val current = self.position.toInt()
        self.jReader.mark(current)
        val byte = self.jReader.read()
        self.jReader.reset()
        self.jReader.mark(0)
        return byte.toLong()
    }

    private fun positionProp(interpreter: Interpreter, arguments: List<Any?>?) = thisInstance(interpreter).position

    private fun putDef(interpreter: Interpreter, arguments: List<Any?>?){
        val self = thisInstance(interpreter)
        if(self.readOnly) throw ArgumentError("Cannot write to the stream, it is readonly.")
        val byte = arguments!![0] as? Long ?: throw ArgumentError("The next byte to write(first argument) must be an integer.")
        self.jWriter!!.write(byte.toInt())
    }

    private fun readonlyProp(interpreter: Interpreter, arguments: List<Any?>?) = thisInstance(interpreter).readOnly

    private fun resetDef(interpreter: Interpreter, arguments: List<Any?>?){
        val self = thisInstance(interpreter)
        self.jReader.reset()
        self.position = 0L
    }

    private fun setPositionDef(interpreter: Interpreter, arguments: List<Any?>?){
        val self = thisInstance(interpreter)
        val position = arguments!![0] as? Long ?: throw ArgumentError("The position(first argument) must be an integer.")
        self.jReader.reset()
        self.jReader.skip(position)
        self.position = position
    }

    private fun skipDef(interpreter: Interpreter, arguments: List<Any?>?) : Long{
        val self = thisInstance(interpreter)
        val numBytes = arguments!![0] as? Long ?: throw ArgumentError("The number of bytes to skip(first argument) must be an integer.")
        val bytesSkipped = self.jReader.skip(numBytes)
        self.position += bytesSkipped
        return bytesSkipped
    }
}