package com.mysidia.ktlox.std.collection

import com.mysidia.ktlox.common.*
import com.mysidia.ktlox.interpreter.ArgumentError
import com.mysidia.ktlox.interpreter.Interpreter
import com.mysidia.ktlox.std.lang.ArrayMetaclass
import com.mysidia.ktlox.std.lang.ObjectClass

object CollectionClass : LoxNativeClass("Collection", ObjectClass){

    init{
        defineNativeMetaclass("Collection class")
        defineNativeGetter("length", this::lengthProp)
        defineNativeMethod("collect", 1, this::collectDef)
        defineNativeMethod("contains", 1, this::containsDef)
        defineNativeMethod("containsAll", 1, this::containsAllDef)
        defineNativeMethod("count", 1, this::countDef)
        defineNativeMethod("detect", 1, this::detectDef)
        defineNativeMethod("each", 1, this::eachDef)
        defineNativeMethod("init", 1, this::initDef)
        defineNativeMethod("isEmpty", 0, this::isEmptyDef)
        defineNativeMethod("reject", 1, this::rejectDef)
        defineNativeMethod("select", 1, this::selectDef)
        defineNativeMethod("toArray", 0, this::toArrayDef)
        defineNativeMethod("toSet", 0, this::toSetDef)
        defineNativeMethod("toList", 0, this::toListDef)
        defineNativeMethod("toString", 0, this::toStringDef)
    }

    override fun initDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxObject{
        throw ArgumentError("Cannot create instance from abstract class Collection.")
    }

    private fun collectDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxCollection<*> {
        val self = interpreter.thisInstance as LoxCollection<*>
        val closure = arguments!![0] as? LoxFunction ?: throw ArgumentError("the closure(first argument) must be a function or closure.")
        val collection = self.emptyCollection()
        for(element in self.elements){
            collection.elements.add(closure.call(interpreter, listOf(element)))
        }
        return collection
    }

    private fun containsDef(interpreter: Interpreter, arguments: List<Any?>?) : Boolean{
        val self = interpreter.thisInstance as LoxCollection<*>
        val element = arguments!![0]
        return self.elements.contains(element)
    }

    private fun containsAllDef(interpreter: Interpreter, arguments: List<Any?>?) : Boolean{
        val self = interpreter.thisInstance as LoxCollection<*>
        val elements = arguments!![0] as? LoxCollection<*> ?: throw ArgumentError("The elements(first arguments) must be a collection.")
        return self.elements.containsAll(elements.elements)
    }

    private fun countDef(interpreter: Interpreter, arguments: List<Any?>?) : Long{
        val self = interpreter.thisInstance as LoxCollection<*>
        val closure = arguments!![0] as? LoxFunction ?: throw ArgumentError("the closure(first argument) must be a function or closure.")
        var num = 0
        for(element in self.elements){
            if(closure.call(interpreter, listOf(element)) == true) num++
        }
        return num.toLong()
    }

    private fun detectDef(interpreter: Interpreter, arguments: List<Any?>?) : Any?{
        val self = interpreter.thisInstance as LoxCollection<*>
        val closure = arguments!![0] as? LoxFunction ?: throw ArgumentError("the closure(first argument) must be a function or closure.")
        for(element in self.elements){
            if(closure.call(interpreter, listOf(element)) == true) return element
        }
        return null
    }

    private fun eachDef(interpreter: Interpreter, arguments: List<Any?>?){
        val self = interpreter.thisInstance as LoxCollection<*>
        val block = arguments!![0] as? LoxFunction ?: throw ArgumentError("the block(first argument) must be a function or closure.")
        for (element in self.elements){
            block.call(interpreter, listOf(element))
        }
    }

    private fun isEmptyDef(interpreter: Interpreter, arguments: List<Any?>?) = (interpreter.thisInstance as LoxCollection<*>).elements.isEmpty()

    private fun lengthProp(interpreter: Interpreter, arguments: List<Any?>?) = (interpreter.thisInstance as LoxCollection<*>).length.toLong()

    private fun rejectDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxCollection<*> {
        val self = interpreter.thisInstance as LoxCollection<*>
        val closure = arguments!![0] as? LoxFunction ?: throw ArgumentError("the closure(first argument) must be a function or closure.")
        val collection = self.emptyCollection()
        for(element in self.elements){
            if(closure.call(interpreter, listOf(element)) == false) collection.elements.add(element)
        }
        return collection
    }

    private fun selectDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxCollection<*> {
        val self = interpreter.thisInstance as LoxCollection<*>
        val closure = arguments!![0] as? LoxFunction ?: throw ArgumentError("the closure(first argument) must be a function or closure.")
        val collection = self.emptyCollection()
        for(element in self.elements){
            if(closure.call(interpreter, listOf(element)) == true) collection.elements.add(element)
        }
        return collection
    }

    private fun toArrayDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxArray{
        val self = interpreter.thisInstance as LoxCollection<*>
        return ArrayMetaclass.createFromCollection(self.elements)
    }

    private fun toListDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxList {
        val self = interpreter.thisInstance as LoxCollection<*>
        return LoxList(self.elements.toMutableList())
    }

    private fun toSetDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxSet {
        val self = interpreter.thisInstance as LoxCollection<*>
        return LoxSet(self.elements.toMutableSet())
    }

    private fun toStringDef(interpreter: Interpreter, arguments: List<Any?>?) = interpreter.thisInstance.toString()
}