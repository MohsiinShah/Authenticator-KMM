package com.mohsin.auth.domain.otp

interface OtpGenerator {

    fun generate(secret: ByteArray, value: Long, algorithm: DigestAlgorithm, digits: Int): String

}