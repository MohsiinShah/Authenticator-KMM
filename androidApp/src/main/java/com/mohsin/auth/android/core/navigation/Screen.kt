package com.mohsin.auth.android.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen : NavKey {

    @Serializable
    data object SplashScreen : Screen

    @Serializable
    data object OnboardingScreen : Screen

    @Serializable
    data object DashboardScreen : Screen

    @Serializable
    data object AddAccountManuallyScreen : Screen

    @Serializable
    data object AddAccountOptionsScreen : Screen

    @Serializable
    data object QRScanScreen : Screen
    @Serializable
    data class ZoomBook(val book: Book) : Screen

}

@Serializable
data class Book(val id: String)
