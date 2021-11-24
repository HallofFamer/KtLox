package com.mysidia.ktlox.std.collection

import com.mysidia.ktlox.common.*
import com.mysidia.ktlox.interpreter.ArgumentError
import com.mysidia.ktlox.interpreter.Interpreter

object ListClass : LoxNativeClass("List", CollectionClass){

    init{
        defineNativeMetaclass("List class")
        defineNativeMethod("add", 1, this::addDef)
        defineNativeMethod("addAll", 1, this::addAllDef)
        defineNativeMethod("clear", 0, this::clearDef)
        defineNativeMethod("eachIndex", 1, this::eachIndexDef)
        defineNativeMethod("get", 1, this::getDef)
        defineNativeMethod("indexOf", 1, this::indexOfDef)
        defineNativeMethod("lastIndexOf", 1, this::lastIndexOfDef)
        defineNativeMethod("remove", 1, this::removeDef)
        defineNativeMethod("removeAll", 1, this::removeAllDef)
        defineNativeMethod("set", 2, this::setDef)
    }

    override val arity = 1

    override val isNative = true

    override fun initDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxList {
        val thisInstance = interpreter.thisInstance as? LoxList ?: LoxList()
        val collection = arguments!![0] as? LoxCollection<*>
        thisInstance.elements = mutableListOf()
        collection?.let { thisInstance.elements.addAll(it.elements) }
        return thisInstance
    }

    override fun new(klass: LoxClass) = LoxList(klass)

    private fun addDef(interpreter: Interpreter, arguments: List<Any?>?){
        val self = interpreter.thisInstance as LoxList
        val element = arguments!![0]
        self.elements.add(element)
    }

    private fun addAllDef(interpreter: Interpreter, arguments: List<Any?>?){
        val self = interpreter.thisInstance as LoxList
        val collection = arguments!![0] as? LoxCollection<*> ?: throw ArgumentError("the collection(first argument) must be an instance of Collection.")
        self.elements.addAll(collection.elements)
    }

    private fun clearDef(interpreter: Interpreter, arguments: List<Any?>?) = (interpreter.thisInstance as LoxList).elements.clear()

    private fun eachIndexDef(interpreter: Interpreter, arguments: List<Any?>?){
        val self = interpreter.thisInstance as LoxList
        val block = arguments!![0] as? LoxFunction ?: throw ArgumentError("the block(first argument) must be a function or closure.")
        for(index in 0..self.length){
            block.call(interpreter, listOf(index))
        }
    }

    private fun getDef(interpreter: Interpreter, arguments: List<Any?>?) : Any?{
        val self = interpreter.thisInstance as LoxList
        val index = arguments!![0] as? Long ?: throw ArgumentError("the index(first argument) must be an integer.")
        if(index < 0 || index >= self.length) throw ArgumentError("the index(first argument) is out of bound.");
        return self.elements[index.toInt()]
    }

    private fun indexOfDef(interpreter: Interpreter, arguments: List<Any?>?) : Long{
        val self = interpreter.thisInstance as LoxList
        val element = arguments!![0]
        return self.elements.indexOf(element).toLong()
    }

    private fun lastIndexOfDef(interpreter: Interpreter, arguments: List<Any?>?) : Long{
        val self = interpreter.thisInstance as LoxList
        val element = arguments!![0]
        return self.elements.lastIndexOf(element).toLong()
    }

    private fun removeDef(interpreter: Interpreter, arguments: List<Any?>?) : Any?{
        val self = interpreter.thisInstance as LoxList
        val element = arguments!![0]
        self.elements.remove(element)
        return element
    }

    private fun removeAllDef(interpreter: Interpreter, arguments: List<Any?>?){
        val self = interpreter.thisInstance as LoxList
        val collection = arguments!![0] as? LoxCollection<*> ?: throw ArgumentError("the collection(first argument) must be an instance of Collection.")
        self.elements.removeAll(collection.elements)
    }

    private fun setDef(interpreter: Interpreter, arguments: List<Any?>?){
        val self = interpreter.thisInstance as LoxList
        val index = arguments!![0] as? Long ?: throw ArgumentError("the index(first argument) must be an integer.")
        val element = arguments[1]
        self.elements[index.toInt()] = element
    }
}