package com.mohsin.auth.domain.time

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Duration.Companion.seconds


object TotpTimer {
    private val listeners = HashSet<TotpListener>()
    private var timerJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)
    private val mutex = Mutex()

    fun start() {
        if (timerJob?.isActive == true) return

        timerJob = scope.launch {
            while (isActive) {
                tick()
                delay(1.seconds)
            }
        }
    }

    fun subscribe(listener: TotpListener) {
        scope.launch {
            mutex.withLock {
                listeners.add(listener)
            }
        }
    }

    fun unsubscribeAll(){
        scope.launch {
            mutex.withLock {
                listeners.clear()
                timerJob?.cancel()
            }
        }
    }

    fun unsubscribe(listener: TotpListener) {
        scope.launch {
            mutex.withLock {
                listeners.remove(listener)
            }
        }
    }

    private suspend fun tick() {
        mutex.withLock {
            try {
                listeners.forEach { it.onTick() }
            } catch (th: Throwable) {
                th.printStackTrace()
            }
        }
    }
}

interface TotpListener {
    fun onTick()
}