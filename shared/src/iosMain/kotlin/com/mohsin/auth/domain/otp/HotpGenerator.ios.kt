package com.mohsin.auth.domain.otp

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.usePinned
import platform.CoreCrypto.CCHmac
import platform.CoreCrypto.CC_SHA1_DIGEST_LENGTH
import platform.CoreCrypto.CC_SHA256_DIGEST_LENGTH
import platform.CoreCrypto.CC_SHA512_DIGEST_LENGTH
import platform.CoreCrypto.kCCHmacAlgSHA1
import platform.CoreCrypto.kCCHmacAlgSHA256
import platform.CoreCrypto.kCCHmacAlgSHA512

@OptIn(ExperimentalForeignApi::class)
actual fun hmac(algorithm: DigestAlgorithm, key: ByteArray, value: ByteArray): ByteArray {
    val algo = when (algorithm.value.uppercase()) {
        "SHA1"   -> kCCHmacAlgSHA1
        "SHA256" -> kCCHmacAlgSHA256
        "SHA512" -> kCCHmacAlgSHA512
        else -> error("Unsupported algorithm: ${algorithm.value}")
    }

    val macLength = when (algo) {
        kCCHmacAlgSHA1 -> CC_SHA1_DIGEST_LENGTH
        kCCHmacAlgSHA256 -> CC_SHA256_DIGEST_LENGTH
        kCCHmacAlgSHA512 -> CC_SHA512_DIGEST_LENGTH
        else -> error("Unsupported algorithm: ${algorithm.value}")
    }

    return ByteArray(macLength.toInt()).apply {
        usePinned { resultPinned ->
            key.usePinned { keyPinned ->
                value.usePinned { valuePinned ->
                    CCHmac(
                        algo,
                        keyPinned.addressOf(0), key.size.convert(),
                        valuePinned.addressOf(0), value.size.convert(),
                        resultPinned.addressOf(0)
                    )
                }
            }
        }
    }
}

actual fun longToBytes(value: Long): ByteArray {
    val buffer = ByteArray(8)
    for (i in 0..7) {
        buffer[7 - i] = ((value ushr (i * 8)) and 0xFF).toByte()
    }
    return buffer
}