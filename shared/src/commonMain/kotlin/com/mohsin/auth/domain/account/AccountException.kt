package com.mohsin.auth.domain.account

class AccountCreationException(val kind: CreationError) : RuntimeException()

enum class CreationError {
    UNDEFINED,
    ALREADY_EXISTS,
    INVALID_URI,
    UNDEFINED_TYPE,
    SHORT_LABEL,
    SHORT_SECRET,
    UNSUPPORTED_ALGORITHM,
    INVALID_INTERVAL,
    INVALID_COUNTER,

    INTERVAL_MAX_LIMIT;

    fun asException() = AccountCreationException(this)
}