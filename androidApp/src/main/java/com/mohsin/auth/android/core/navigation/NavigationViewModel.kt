package com.mohsin.auth.android.core.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class NavigationViewModel : ViewModel() {
    val backStack = mutableStateListOf<Screen>(Screen.SplashScreen)

    val currentScreen: Screen
        get() = backStack.last()

    fun moveToScreen(screen: Screen){
        backStack.add(screen)
    }

    fun pop(){
        if(!backStack.isEmpty()) {
            backStack.removeAt(backStack.lastIndex)
        }
    }

    fun popTo(screen: Screen) {
        while (backStack.size > 1 && backStack.last() != screen) {
            backStack.removeAt(backStack.lastIndex)
        }
    }
}