package com.mysidia.ktlox.std.lang

import com.mysidia.ktlox.common.LoxModule
import com.mysidia.ktlox.interpreter.Environment

object LangModule : LoxModule{

    override fun registerModule(env: Environment) {
        env.define("Object", ObjectClass)
        env.define("Nil", NilClass)
        env.define("Boolean", BooleanClass)
        env.define("False", FalseClass)
        env.define("True", TrueClass)
        env.define("Number", NumberClass)
        env.define("Int", IntClass)
        env.define("Float", FloatClass)
        env.define("String", StringClass)
        env.define("Function", FunctionClass)
        env.define("Class", ClassClass)
        env.define("Trait", TraitClass)
        env.define("Array", ArrayClass)
    }
}