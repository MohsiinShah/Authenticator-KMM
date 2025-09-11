package com.mohsin.auth.domain.account

import kotlinx.serialization.Serializable
import com.mohsin.auth.domain.otp.DigestAlgorithm

@Serializable
sealed class Account {

    abstract val label: String
    abstract var name: String
    abstract val issuer: String?
    abstract val secret: ByteArray
    abstract val algorithm: DigestAlgorithm
    abstract val digits: Int

    abstract var password: String

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        other as Account

        return label == other.label
                && name == other.name
                && issuer == other.issuer
                && secret.contentEquals(other.secret)
                && algorithm == other.algorithm
                && digits == other.digits
    }

    override fun hashCode(): Int {
        var result = label.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + (issuer?.hashCode() ?: 0)
        result = 31 * result + secret.contentHashCode()
        result = 31 * result + algorithm.hashCode()
        result = 31 * result + digits
        return result
    }

    override fun toString(): String {
        val secretPreview = secret.joinToString("")
            .take(16) + if (secret.size > 8) "..." else ""

        return "Account(" +
                "label='$label', " +
                "name='$name', " +
                "issuer=$issuer, " +
                "secret=$secretPreview, " +
                "algorithm=$algorithm, " +
                "digits=$digits, " +
                "password=$password" +
                ")"
    }

}