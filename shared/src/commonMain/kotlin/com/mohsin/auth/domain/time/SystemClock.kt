package com.mohsin.auth.domain.time

import kotlin.time.ExperimentalTime

object SystemClock : Clock {

    @OptIn(ExperimentalTime::class)
    override fun epochSeconds(): Long {
        return kotlin.time.Clock.System.now().toEpochMilliseconds() / 1000
    }

}