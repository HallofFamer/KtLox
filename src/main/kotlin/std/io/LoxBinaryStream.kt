package com.mysidia.ktlox.std.io

import com.mysidia.ktlox.common.*
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.Closeable

class LoxBinaryStream(klass: LoxClass = BinaryStreamClass) : LoxObject(klass), Closeable{

    var position = 0L

    lateinit var jReader: BufferedInputStream

    var jWriter: BufferedOutputStream? = null

    val readOnly get() = jWriter == null

    constructor(jReader: BufferedInputStream, klass: LoxClass = BinaryStreamClass) : this(klass){
        this.jReader = jReader
    }

    constructor(jReader: BufferedInputStream, jWriter: BufferedOutputStream, klass: LoxClass = BinaryStreamClass) : this(klass){
        this.jReader = jReader
        this.jWriter = jWriter
    }

    override fun close(){
        this.jReader.close()
        this.jWriter?.close()
    }
}