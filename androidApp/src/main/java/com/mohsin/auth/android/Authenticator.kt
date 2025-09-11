package com.mohsin.auth.android

import android.app.Application
import com.mohsin.auth.PlatformContext
import com.mohsin.auth.core.di.initKoin
import com.mohsin.auth.domain.time.TotpTimer
import org.koin.android.ext.koin.androidContext

class Authenticator: Application() {
    override fun onCreate() {
        super.onCreate()
        PlatformContext.init(this)
        TotpTimer.start()
        initKoin {
            androidContext(this@Authenticator)
        }
    }
}