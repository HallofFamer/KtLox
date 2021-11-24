package com.mysidia.ktlox.std.io

import com.mysidia.ktlox.common.*
import com.mysidia.ktlox.interpreter.ArgumentError
import com.mysidia.ktlox.interpreter.Interpreter
import com.mysidia.ktlox.std.lang.ObjectClass
import java.io.File

object FileClass : LoxNativeClass("File", ObjectClass, null, FileMetaclass){

    init{
        defineNativeGetter("absolutePath", this::absolutePathProp)
        defineNativeGetter("ext", this::extProp)
        defineNativeGetter("length", this::lengthProp)
        defineNativeGetter("name", this::nameProp)
        defineNativeGetter("path", this::pathProp)
        defineNativeMethod("delete", 0, this::deleteDef)
        defineNativeMethod("exists", 0, this::existsDef)
        defineNativeMethod("init", 1, this::initDef)
        defineNativeMethod("isFile", 0, this::isFileDef)
        defineNativeMethod("isHidden", 0, this::isHiddenDef)
        defineNativeMethod("lastModified", 0, this::lastModifiedDef)
        defineNativeMethod("rename", 1, this::renameDef)
        defineNativeMethod("renameTo", 1, this::renameToDef)
    }

    override val arity = 1

    override val isNative = true

    override fun initDef(interpreter: Interpreter, arguments: List<Any?>?): LoxFile{
        val thisInstance = interpreter.thisInstance as? LoxFile ?: LoxFile()
        val fileName = arguments!![0] as? String ?: ""
        thisInstance.jFile = File(fileName)
        return thisInstance
    }

    override fun new(klass: LoxClass) = LoxFile(klass)

    override fun thisInstance(interpreter: Interpreter) = interpreter.thisInstance as LoxFile

    private fun absolutePathProp(interpreter: Interpreter, arguments: List<Any?>?) = thisInstance(interpreter).jFile.absolutePath

    private fun deleteDef(interpreter: Interpreter, arguments: List<Any?>?) = thisInstance(interpreter).jFile.delete()

    private fun existsDef(interpreter: Interpreter, arguments: List<Any?>?) = thisInstance(interpreter).jFile.exists()

    private fun extProp(interpreter: Interpreter, arguments: List<Any?>?) = thisInstance(interpreter).jFile.extension

    private fun isFileDef(interpreter: Interpreter, arguments: List<Any?>?) = thisInstance(interpreter).jFile.isFile

    private fun isHiddenDef(interpreter: Interpreter, arguments: List<Any?>?) = thisInstance(interpreter).jFile.isHidden

    private fun lastModifiedDef(interpreter: Interpreter, arguments: List<Any?>?) = thisInstance(interpreter).jFile.lastModified()

    private fun lengthProp(interpreter: Interpreter, arguments: List<Any?>?) = thisInstance(interpreter).jFile.length()

    private fun nameProp(interpreter: Interpreter, arguments: List<Any?>?) = thisInstance(interpreter).jFile.name

    private fun pathProp(interpreter: Interpreter, arguments: List<Any?>?) = thisInstance(interpreter).jFile.path

    private fun renameDef(interpreter: Interpreter, arguments: List<Any?>?) : Boolean{
        val self = thisInstance(interpreter)
        val fileName = arguments!![0] as? String ?: throw ArgumentError("the file name(first argument) must be a string.")
        if(fileName == self.jFile.name) throw ArgumentError("the new file name is the same as current file name.")
        return self.jFile.renameTo(File(fileName))
    }

    private fun renameToDef(interpreter: Interpreter, arguments: List<Any?>?) : Boolean{
        val self = thisInstance(interpreter)
        val file = arguments!![0] as? LoxFile ?: throw ArgumentError("the destination file must be an instance of File.")
        if(file.jFile.name == self.jFile.name) throw ArgumentError("the new file appears to be the same as the current file.")
        return self.jFile.renameTo(file.jFile)
    }
}