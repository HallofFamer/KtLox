package com.mysidia.ktlox.std.lang

import com.mysidia.ktlox.common.*
import com.mysidia.ktlox.interpreter.ArgumentError
import com.mysidia.ktlox.interpreter.Interpreter

object ObjectClass : LoxNativeClass("Object", null){

    init{
        defineNativeMetaclass("Object class")
        defineNativeGetter("className", this::classNameProp)
        defineNativeMethod("getClass", 0, this::getClassDef)
        defineNativeMethod("hashCode", 0, this::hashCodeDef)
        defineNativeMethod("hasTrait", 1, this::hasTraitDef)
        defineNativeMethod("instanceOf", 1, this::instanceOfDef)
        defineNativeMethod("isBoolean", 0, this::isBooleanDef)
        defineNativeMethod("isClass", 0, this::isClassDef)
        defineNativeMethod("isNil", 0, this::isNilDef)
        defineNativeMethod("isNilOrEmpty", 0, this::isNilOrEmptyDef)
        defineNativeMethod("isNumber", 0, this::isNumberDef)
        defineNativeMethod("isString", 0, this::isStringDef)
        defineNativeMethod("isTrait", 0, this::isTraitDef)
        defineNativeMethod("memberOf", 1, this::memberOfDef)
        defineNativeMethod("respondsTo", 1, this::respondsToDef)
        defineNativeMethod("toString", 0, this::toStringDef)
    }

    override fun new(klass: LoxClass) = LoxObject(klass)

    private fun classNameProp(interpreter: Interpreter, arguments: List<Any?>?) = interpreter.thisInstance.className

    private fun getClassDef(interpreter: Interpreter, arguments: List<Any?>?) = interpreter.thisInstance.klass

    private fun hashCodeDef(interpreter: Interpreter, arguments: List<Any?>?) = interpreter.thisInstance.hashCode()

    private fun hasTraitDef(interpreter: Interpreter, arguments: List<Any?>?) : Boolean{
        val self = interpreter.thisInstance
        val trait = arguments!![0] as? LoxTrait ?: return false
        self.klass?.traits?.forEach {
            if(it == trait || it.parents.contains(trait)) return true
        }
        return false
    }

    private fun instanceOfDef(interpreter: Interpreter, arguments: List<Any?>?) : Boolean{
        val self = interpreter.thisInstance
        val klass = arguments!![0] as? LoxClass ?: return hasTraitDef(interpreter, arguments)
        if(self.className == klass.name) return true

        var superKlass = self.klass
        while(superKlass != null){
            superKlass = superKlass.superclass
            if(klass.name == superKlass?.name) return true
        }
        return false
    }

    private fun isBooleanDef(interpreter: Interpreter, arguments: List<Any?>?) = false

    private fun isClassDef(interpreter: Interpreter, arguments: List<Any?>?) = false

    private fun isNilDef(interpreter: Interpreter, arguments: List<Any?>?) = false

    private fun isNilOrEmptyDef(interpreter: Interpreter, arguments: List<Any?>?) = false

    private fun isNumberDef(interpreter: Interpreter, arguments: List<Any?>?) = false

    private fun isStringDef(interpreter: Interpreter, arguments: List<Any?>?) = false

    private fun isTraitDef(interpreter: Interpreter, arguments: List<Any?>?) = false

    private fun memberOfDef(interpreter: Interpreter, arguments: List<Any?>?) : Boolean{
        val self = interpreter.thisInstance
        val klass = arguments!![0] as? LoxClass ?: return false
        return self.className == klass.name
    }

    private fun respondsToDef(interpreter: Interpreter, arguments: List<Any?>?) : Boolean{
        val self = interpreter.thisInstance
        return when(val method = arguments!![0]){
            is String -> self.klass?.findMethod(method) != null
            is LoxFunction -> method.name?.let { self.klass?.findMethod(it) } != null
            is LoxNativeMethod -> self.klass?.findMethod(method.name) != null
            else -> throw ArgumentError("supplied argument must be a string or a function/method object")
        }
    }

    private fun toStringDef(interpreter: Interpreter, arguments: List<Any?>?) = thisInstance(interpreter).toString()
}