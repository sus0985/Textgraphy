package com.sjh.textography

class ByteManager(private val bytes: ByteArray) {

    private var bitIndex: Int = 0

    private val maxPointer = bytes.size * 8

    fun isEnd(): Boolean = bitIndex >= maxPointer

    fun getCurrentBit(): Int {
        return 0
    }
}