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
    this.let { secret ->
        // Regex for Base32 (RFC 4648) with optional "=" padding
        val base32Regex = Regex("^[A-Z2-7]+=*\$")

        // Invalid character check
        if (secret.any { !it.isLetterOrDigit() && it != '=' }) {
            return "Secret key contains invalid special characters"
        }

        // Check if it's a valid Base32 sequence
        if (!base32Regex.matches(secret)) {
            return "Secret key must only contain A–Z and digits 2–7 (with optional '=' padding)"
        }

        return null // ✅ No error, secret is valid
    }
}
