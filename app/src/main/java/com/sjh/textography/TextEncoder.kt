package com.sjh.textography

class TextEncoder(private val plainByteArray: ByteArray) {

    private val channels = 3
    private val shiftIndex = 4

    private var index = 0
    val isEndOfMessage: Boolean
        get() = plainByteArray.size <= index

    fun getByte(): Byte {
        return plainByteArray[index++]
    }
}