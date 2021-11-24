package com.mysidia.ktlox.std.io

import com.mysidia.ktlox.common.*
import java.io.File

class LoxFile(klass: LoxClass = FileClass) : LoxObject(klass){

    lateinit var jFile : File

    constructor(jFile: File, klass: LoxClass = FileClass) : this(klass){
        this.jFile = jFile
    }

    override fun toString() = if(jFile.isDirectory) "Directory: ${jFile.name}" else "File: ${jFile.name}"
}