package com.mohsin.auth.android.utils

import android.content.Context
import com.mohsin.auth.android.R
import com.mohsin.auth.domain.account.CreationError

val map = hashMapOf(
    CreationError.UNDEFINED to R.string.error_undefined,
    CreationError.ALREADY_EXISTS to R.string.error_exists,
    CreationError.INVALID_URI to R.string.error_invalidUri,
    CreationError.UNDEFINED_TYPE to R.string.error_undefinedType,
    CreationError.SHORT_LABEL to R.string.error_shortLabel,
    CreationError.SHORT_SECRET to R.string.error_shortSecret,
    CreationError.INVALID_INTERVAL to R.string.error_invalidInterval,
    CreationError.INVALID_COUNTER to R.string.error_invalidCounter,
    CreationError.INTERVAL_MAX_LIMIT to R.string.max_interval_should_not_exceed_60_seconds
)

fun CreationError.display(ctx: Context): String {
    return ctx.getString(map[this] ?: R.string.error_undefined)
}

fun String.validateSecretKey(): String? {
    val secret = this.trim().replace(" ", "").uppercase() // Remove spaces

    if (secret.isEmpty()) {
        return "Secret key cannot be empty"
    }

    // Basic format check first
    val base32Regex = Regex("^[A-Z2-7]+=*$")
    if (!base32Regex.matches(secret)) {
        return "Secret key must only contain A-Z, digits 2-7, and optional '=' padding"
    }

    try {
        decodeBase32(secret)
        val secretWithoutPadding = secret.trimEnd('=')

        if (secretWithoutPadding.length < 16) {
            return "Secret key too short. Minimum 16 characters required"
        }

        return null // ✅ Valid and decodeable
    } catch (e: Exception) {
        return "Invalid Base32 encoding: ${e.message}"
    }
}

// Simple Base32 decoder for validation
private fun decodeBase32(input: String): ByteArray {
    val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"
    val cleanInput = input.trimEnd('=')

    if (cleanInput.any { it !in alphabet }) {
        throw IllegalArgumentException("Invalid Base32 character")
    }

    val bits = cleanInput.map { alphabet.indexOf(it).toString(2).padStart(5, '0') }.joinToString("")
    val bytes = mutableListOf<Byte>()

    for (i in bits.indices step 8) {
        if (i + 8 <= bits.length) {
            bytes.add(bits.substring(i, i + 8).toInt(2).toByte())
        }
    }

    return bytes.toByteArray()
}
