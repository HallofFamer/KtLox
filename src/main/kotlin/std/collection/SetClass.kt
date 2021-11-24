package com.mysidia.ktlox.std.collection

import com.mysidia.ktlox.common.*
import com.mysidia.ktlox.interpreter.ArgumentError
import com.mysidia.ktlox.interpreter.Interpreter

object SetClass : LoxNativeClass("Set", CollectionClass) {

    init{
        defineNativeMetaclass("Set class")
        defineNativeMethod("add", 1, this::addDef)
        defineNativeMethod("addAll", 1, this::addAllDef)
        defineNativeMethod("clear", 0, this::clearDef)
        defineNativeMethod("remove", 1, this::removeDef)
        defineNativeMethod("removeAll", 1, this::removeAllDef)
    }

    override val arity = 1

    override val isNative = true

    override fun initDef(interpreter: Interpreter, arguments: List<Any?>?): LoxSet {
        val thisInstance = interpreter.thisInstance as? LoxSet ?: LoxSet()
        val collection = arguments!![0] as? LoxCollection<*>
        thisInstance.elements = mutableSetOf()
        collection?.let { thisInstance.elements.addAll(it.elements) }
        return thisInstance
    }

    override fun new(klass: LoxClass) = LoxSet(klass)

    private fun addDef(interpreter: Interpreter, arguments: List<Any?>?){
        val self = interpreter.thisInstance as LoxSet
        val element = arguments!![0]
        self.elements.add(element)
    }

    private fun addAllDef(interpreter: Interpreter, arguments: List<Any?>?){
        val self = interpreter.thisInstance as LoxSet
        val collection = arguments!![0] as? LoxCollection<*> ?: throw ArgumentError("the collection(first argument) must be an instance of Collection.")
        self.elements.addAll(collection.elements)
    }

    private fun clearDef(interpreter: Interpreter, arguments: List<Any?>?) = (interpreter.thisInstance as LoxSet).elements.clear()

    private fun removeDef(interpreter: Interpreter, arguments: List<Any?>?) : Any?{
        val self = interpreter.thisInstance as LoxSet
        val element = arguments!![0]
        self.elements.remove(element)
        return element
    }

    private fun removeAllDef(interpreter: Interpreter, arguments: List<Any?>?){
        val self = interpreter.thisInstance as LoxSet
        val collection = arguments!![0] as? LoxCollection<*> ?: throw ArgumentError("the collection(first argument) must be an instance of Collection.")
        self.elements.removeAll(collection.elements)
    }
}