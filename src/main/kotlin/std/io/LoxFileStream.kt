package com.mysidia.ktlox.std.io

import com.mysidia.ktlox.common.*
import java.io.*

class LoxFileStream(klass: LoxClass = FileStreamClass) : LoxObject(klass), Closeable{

    var position = 0L

    lateinit var jReader: BufferedReader

    var jWriter: BufferedWriter? = null

    val readOnly get() = jWriter == null

    constructor(jReader: BufferedReader, klass: LoxClass = FileStreamClass) : this(klass){
        this.jReader = jReader
    }

    constructor(jReader: BufferedReader, jWriter: BufferedWriter, klass: LoxClass = FileStreamClass) : this(klass){
        this.jReader = jReader
        this.jWriter = jWriter
    }

    override fun close(){
        this.jReader.close()
        this.jWriter?.close()
    }
}