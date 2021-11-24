package com.mysidia.ktlox.std.collection

import com.mysidia.ktlox.common.LoxModule
import com.mysidia.ktlox.interpreter.Environment

object CollectionModule : LoxModule{

    override fun registerModule(env: Environment) {
        env.define("Collection", CollectionClass)
        env.define("List", ListClass)
        env.define("Set", SetClass)
        env.define("Map", MapClass)
        env.define("Entry", EntryClass)
    }
}