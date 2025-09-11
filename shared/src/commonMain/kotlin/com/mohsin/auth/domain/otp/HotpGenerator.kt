package com.mohsin.auth.domain.otp

import kotlin.math.pow

open class HotpGenerator : OtpGenerator {

    override fun generate(secret: ByteArray, value: Long, algorithm: DigestAlgorithm, digits: Int): String {
        val hash = hmac(algorithm, secret, value.toBytes())
        val offset = hash.last().toInt() and 0x0f
        val truncated = truncate(hash, offset)
        val code = truncated % 10.0.pow(digits).toInt()
        return code.toString().padStart(digits, '0')
    }

    private fun truncate(hash: ByteArray, offset: Int): Int {
        return ((hash[offset].toInt() and 0x7f) shl 24) or
                ((hash[offset+1].toInt() and 0xff) shl 16) or
                ((hash[offset+2].toInt() and 0xff) shl 8) or
                (hash[offset+3].toInt() and 0xff)
    }

    private fun Long.toBytes(): ByteArray = longToBytes(this)

    companion object {
        val INSTANCE = HotpGenerator()
    }
}

// 👇 expect declarations
expect fun hmac(algorithm: DigestAlgorithm, key: ByteArray, value: ByteArray): ByteArray
expect fun longToBytes(value: Long): ByteArray
