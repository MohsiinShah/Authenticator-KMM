package com.mohsin.auth.domain.otp

import java.nio.ByteBuffer
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

actual fun hmac(algorithm: DigestAlgorithm, key: ByteArray, value: ByteArray): ByteArray {
    return Mac.getInstance("Hmac${algorithm.value}").run {
        init(SecretKeySpec(key, "RAW"))
        doFinal(value)
    }
}

actual fun longToBytes(value: Long): ByteArray {
    return ByteBuffer.allocate(8).putLong(value).array()
}