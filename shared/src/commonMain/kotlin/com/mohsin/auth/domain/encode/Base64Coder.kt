package com.mohsin.auth.domain.encode

interface Base64Coder {

    fun encode(bytes: ByteArray): String

    fun decode(value: String): ByteArray

}