package com.mohsin.auth.domain.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import com.mohsin.auth.domain.otp.DigestAlgorithm
import com.mohsin.auth.domain.otp.HotpGenerator
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Serializable
@SerialName("hotp")
class HotpAccount(
    override val label: String,
    override var name: String,
    override val issuer: String?,
    override val secret: ByteArray,
    override val algorithm: DigestAlgorithm,
    override val digits: Int,
    var counter: Long
) : Account() {

    companion object {
        private const val UPDATE_COOLDOWN = 3000
    }

    @Transient
    override var password: String = generate()

    @Transient
    private var lastUpdate: Long = 0

    @OptIn(ExperimentalTime::class)
    fun increment(): Boolean {
        if (isReadyForUpdate()) {
            counter += 1
            password = generate()
            lastUpdate = Clock.System.now().toEpochMilliseconds()
            return true
        }
        return false
    }

    private fun generate(): String {
        return HotpGenerator.INSTANCE.generate(secret, counter, algorithm, digits)
    }

    @OptIn(ExperimentalTime::class)
    private fun isReadyForUpdate(): Boolean {
        return Clock.System.now().toEpochMilliseconds() > lastUpdate + UPDATE_COOLDOWN
    }

}