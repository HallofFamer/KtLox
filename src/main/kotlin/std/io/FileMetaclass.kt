package com.mysidia.ktlox.std.io

import com.mysidia.ktlox.common.*
import com.mysidia.ktlox.interpreter.ArgumentError
import com.mysidia.ktlox.interpreter.Interpreter
import com.mysidia.ktlox.std.lang.ClassClass
import java.io.*

object FileMetaclass : LoxNativeClass("File class", ClassClass, null, ClassClass){

    init{
        defineNativeMethod("create", 1, this::createDef)
        defineNativeMethod("open", 2, this::openDef)
        defineNativeMethod("openBinary", 2, this::openBinaryDef)
    }

    private fun createDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxFile{
        val fileName = arguments!![0] as String
        val file = File(fileName)
        if(file.isFile) throw ArgumentError("File $fileName already exists.")
        file.createNewFile()
        return LoxFile(file)
    }

    private fun openDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxFileStream{
        val fileName = arguments!![0] as String
        val file = File(fileName)
        if(!file.isFile) throw ArgumentError("The supplied file name is invalid.")
        val readOnly = arguments[1] as Boolean
        return if(readOnly) LoxFileStream(file.bufferedReader())
               else LoxFileStream(file.bufferedReader(), file.bufferedWriter())
    }

    private fun openBinaryDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxBinaryStream{
        val fileName = arguments!![0] as String
        val file = File(fileName)
        if(!file.isFile) throw ArgumentError("The supplied file name is invalid.")
        val readOnly = arguments[1] as Boolean
        return if(readOnly) LoxBinaryStream(BufferedInputStream(FileInputStream(file)))
               else LoxBinaryStream(BufferedInputStream(FileInputStream(file)), BufferedOutputStream(FileOutputStream(file)))
    }
}