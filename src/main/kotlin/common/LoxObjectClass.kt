package com.mysidia.ktlox.common

import com.mysidia.ktlox.interpreter.Interpreter

object LoxObjectClass : LoxNativeClass("Object", null){

    init{
        defineNativeMetaclass("Object class")
        defineNativeGetter("className", this::classNameProp)
        defineNativeMethod("getClass", 0, this::getClassDef)
        defineNativeMethod("getClassName", 0, this::getClassNameDef)
        defineNativeMethod("hashCode", 0, this::hashCodeDef)
        defineNativeMethod("instanceOf", 1, this::instanceOfDef)
        defineNativeMethod("isBoolean", 0, this::isBooleanDef)
        defineNativeMethod("isClass", 0, this::isClassDef)
        defineNativeMethod("isNil", 0, this::isNilDef)
        defineNativeMethod("isNilOrEmpty", 0, this::isNilOrEmptyDef)
        defineNativeMethod("isNumber", 0, this::isNumberDef)
        defineNativeMethod("isString", 0, this::isStringDef)
        defineNativeMethod("memberOf", 1, this::memberOfDef)
        defineNativeMethod("toString", 0, this::toStringDef)
    }

    private fun classNameProp(interpreter: Interpreter, arguments: List<Any?>?) = interpreter.thisInstance.className

    private fun getClassDef(interpreter: Interpreter, arguments: List<Any?>?) = interpreter.thisInstance.klass

    private fun getClassNameDef(interpreter: Interpreter, arguments: List<Any?>?) = interpreter.thisInstance.className

    private fun hashCodeDef(interpreter: Interpreter, arguments: List<Any?>?) = interpreter.thisInstance.hashCode()

    private fun instanceOfDef(interpreter: Interpreter, arguments: List<Any?>?) : Boolean{
        val self = interpreter.thisInstance
        val klass = arguments!![0] as? LoxClass ?: return false
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

    private fun memberOfDef(interpreter: Interpreter, arguments: List<Any?>?) : Boolean{
        val self = interpreter.thisInstance
        val klass = arguments!![0] as? LoxClass ?: return false
        return self.className == klass.name
    }

    private fun toStringDef(interpreter: Interpreter, arguments: List<Any?>?) = "instance of class ${interpreter.thisInstance.className}"
}