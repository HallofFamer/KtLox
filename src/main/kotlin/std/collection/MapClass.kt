package com.mysidia.ktlox.std.collection

import com.mysidia.ktlox.common.*
import com.mysidia.ktlox.interpreter.ArgumentError
import com.mysidia.ktlox.interpreter.Interpreter
import com.mysidia.ktlox.std.lang.ArrayMetaclass

object MapClass : LoxNativeClass("Map", CollectionClass){

    init{
        defineNativeMetaclass("Map class")
        defineNativeGetter("length", this::lengthProp)
        defineNativeMethod("add", 1, this::addDef)
        defineNativeMethod("addAll", 1, this::addAllDef)
        defineNativeMethod("clear", 0, this::clearDef)
        defineNativeMethod("collect", 1, this::collectDef)
        defineNativeMethod("contains", 1, this::containsDef)
        defineNativeMethod("containsAll", 1, this::containsAllDef)
        defineNativeMethod("containsKey", 1, this::containsKeyDef)
        defineNativeMethod("containsValue", 1, this::containsValueDef)
        defineNativeMethod("detect", 1, this::detectDef)
        defineNativeMethod("each", 1, this::eachDef)
        defineNativeMethod("eachKey", 1, this::eachKeyDef)
        defineNativeMethod("eachValue", 1, this::eachValueDef)
        defineNativeMethod("isEmpty", 0, this::isEmptyDef)
        defineNativeMethod("keys", 0, this::keysDef)
        defineNativeMethod("put", 2, this::putDef)
        defineNativeMethod("putAll", 1, this::putAllDef)
        defineNativeMethod("reject", 1, this::rejectDef)
        defineNativeMethod("remove", 1, this::removeDef)
        defineNativeMethod("removeKey", 1, this::removeKeyDef)
        defineNativeMethod("select", 1, this::selectDef)
        defineNativeMethod("toList", 0, this::toListDef)
        defineNativeMethod("toArray", 0, this::toArrayDef)
        defineNativeMethod("toList", 0, this::toListDef)
        defineNativeMethod("toSet", 0, this::toSetDef)
        defineNativeMethod("toString", 0, this::toStringDef)
        defineNativeMethod("values", 0, this::valuesDef)
    }

    override val arity = 1

    override val isNative = true

    override fun initDef(interpreter: Interpreter, arguments: List<Any?>?): LoxMap {
        val thisInstance = interpreter.thisInstance as? LoxMap ?: LoxMap()
        val collection = arguments!![0] as? LoxMap
        thisInstance.elements = mutableMapOf()
        collection?.let { thisInstance.elements.putAll(collection.elements) }
        return thisInstance
    }

    override fun new(klass: LoxClass) = LoxMap(klass)

    override fun thisInstance(interpreter: Interpreter) = interpreter.thisInstance as LoxMap

    private fun addDef(interpreter: Interpreter, arguments: List<Any?>?){
        val self = thisInstance(interpreter)
        val entry = arguments!![0] as? LoxEntry ?: throw ArgumentError("the element(first argument) must be an instance of Entry.")
        self.elements[entry.key] = entry.value
    }

    private fun addAllDef(interpreter: Interpreter, arguments: List<Any?>?){
        val self = thisInstance(interpreter)
        val entries = arguments!![0] as? LoxCollection<*> ?: throw ArgumentError("the collection(first argument) must be an instance of Collection.")
        for(element in entries.elements){
            if(element !is LoxEntry) throw ArgumentError("The elements in the collection must be an instance of Entry")
            self.elements[element.key] = element.value
        }
    }

    private fun clearDef(interpreter: Interpreter, arguments: List<Any?>?) = thisInstance(interpreter).elements.clear()

    private fun collectDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxMap {
        val self = thisInstance(interpreter)
        val closure = arguments!![0] as? LoxFunction ?: throw ArgumentError("the closure(first argument) must be a function or closure.")
        val map = self.emptyCollection()
        for((key, value) in self.elements){
            map.elements[key] = closure.call(interpreter, listOf(key, value))
        }
        return map
    }

    private fun containsDef(interpreter: Interpreter, arguments: List<Any?>?) : Boolean{
        val self = thisInstance(interpreter)
        val entry = arguments!![0] as LoxEntry
        return (self.elements.contains(entry.key) && self.elements[entry.key] == entry.value)
    }

    private fun containsAllDef(interpreter: Interpreter, arguments: List<Any?>?) : Boolean{
        val self = thisInstance(interpreter)
        val collection = arguments!![0] as? LoxMap ?: throw ArgumentError("the collection(first argument) must be an instance of Map.")
        for((key, _) in collection.elements){
            if(!self.elements.containsKey(key)) return false
        }
        return true
    }

    private fun containsKeyDef(interpreter: Interpreter, arguments: List<Any?>?) : Boolean{
        val self = thisInstance(interpreter)
        val key = arguments!![0] ?: throw ArgumentError("the key(first argument) cannot be nil")
        return self.elements.containsKey(key)
    }

    private fun containsValueDef(interpreter: Interpreter, arguments: List<Any?>?) : Boolean{
        val self = thisInstance(interpreter)
        val value = arguments!![0]
        return self.elements.containsValue(value)
    }

    private fun detectDef(interpreter: Interpreter, arguments: List<Any?>?) : Any?{
        val self = thisInstance(interpreter)
        val closure = arguments!![0] as? LoxFunction ?: throw ArgumentError("the closure(first argument) must be a function or closure.")
        for((key, value) in self.elements){
            if(closure.call(interpreter, listOf(key, value)) == true) return value
        }
        return null
    }

    private fun eachDef(interpreter: Interpreter, arguments: List<Any?>?){
        val self = thisInstance(interpreter)
        val block = arguments!![0] as? LoxFunction ?: throw ArgumentError("the block(first argument) must be a function or closure.")
        for((key, value) in self.elements){
            block.call(interpreter, listOf(key, value))
        }
    }

    private fun eachKeyDef(interpreter: Interpreter, arguments: List<Any?>?){
        val self = thisInstance(interpreter)
        val block = arguments!![0] as? LoxFunction ?: throw ArgumentError("the block(first argument) must be a function or closure.")
        for((key, _) in self.elements){
            block.call(interpreter, listOf(key))
        }
    }

    private fun eachValueDef(interpreter: Interpreter, arguments: List<Any?>?){
        val self = thisInstance(interpreter)
        val block = arguments!![0] as? LoxFunction ?: throw ArgumentError("the block(first argument) must be a function or closure.")
        for((_, value) in self.elements){
            block.call(interpreter, listOf(value))
        }
    }

    private fun isEmptyDef(interpreter: Interpreter, arguments: List<Any?>?) = thisInstance(interpreter).elements.isEmpty()

    private fun lengthProp(interpreter: Interpreter, arguments: List<Any?>?) = thisInstance(interpreter).length.toLong()

    private fun keysDef(interpreter: Interpreter, arguments: List<Any?>?) = LoxSet(thisInstance(interpreter).elements.keys as MutableSet<Any?>)

    private fun putDef(interpreter: Interpreter, arguments: List<Any?>?){
        val self = thisInstance(interpreter)
        val key = arguments!![0] ?: throw ArgumentError("the key(first argument) cannot be nil")
        val value = arguments[1]
        self.elements[key] = value
    }

    private fun putAllDef(interpreter: Interpreter, arguments: List<Any?>?){
        val self = thisInstance(interpreter)
        val collection = arguments!![0] as? LoxMap ?: throw ArgumentError("the collection(first argument) must be an instance of Map.")
        self.elements.putAll(collection.elements)
    }

    private fun rejectDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxMap{
        val self = thisInstance(interpreter)
        val closure = arguments!![0] as? LoxFunction ?: throw ArgumentError("the closure(first argument) must be a function or closure.")
        val map = self.emptyCollection()
        for((key, value) in self.elements){
            if(closure.call(interpreter, listOf(key, value)) == false) map.elements[key] = value
        }
        return map
    }

    private fun removeDef(interpreter: Interpreter, arguments: List<Any?>?) : Any?{
        val self = thisInstance(interpreter)
        val entry = arguments!![0] as? LoxEntry ?: throw ArgumentError("the element(first argument) must be an instance of Entry.")
        return self.elements.remove(entry.key)
    }

    private fun removeKeyDef(interpreter: Interpreter, arguments: List<Any?>?) : Any?{
        val self = thisInstance(interpreter)
        val key = arguments!![0] ?: throw ArgumentError("the key(first argument) cannot be nil")
        return self.elements.remove(key)
    }

    private fun selectDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxMap{
        val self = thisInstance(interpreter)
        val closure = arguments!![0] as? LoxFunction ?: throw ArgumentError("the closure(first argument) must be a function or closure.")
        val map = self.emptyCollection()
        for((key, value) in self.elements){
            if(closure.call(interpreter, listOf(key, value)) == true) map.elements[key] = value
        }
        return map
    }

    private fun toArrayDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxArray{
        val self = thisInstance(interpreter)
        val elements = mutableListOf<Any?>()
        for((key, value) in self.elements){
            elements.add(LoxEntry(key, value))
        }
        return ArrayMetaclass.createFromList(elements)
    }

    private fun toListDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxList{
        val self = thisInstance(interpreter)
        val elements = mutableListOf<Any?>()
        for((key, value) in self.elements){
            elements.add(LoxEntry(key, value))
        }
        return LoxList(elements, ListClass)
    }

    private fun toSetDef(interpreter: Interpreter, arguments: List<Any?>?) : LoxSet{
        val self = thisInstance(interpreter)
        val elements = mutableSetOf<Any?>()
        for((key, _) in self.elements){
            elements.add(key)
        }
        return LoxSet(elements)
    }

    private fun toStringDef(interpreter: Interpreter, arguments: List<Any?>?) = thisInstance(interpreter).toString()

    private fun valuesDef(interpreter: Interpreter, arguments: List<Any?>?) = LoxSet(thisInstance(interpreter).elements.values as MutableSet<Any?>)
}