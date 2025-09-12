package com.mohsin.auth.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mohsin.auth.android.components.AppSnackBar
import com.mohsin.auth.android.components.ConfirmationDialog
import com.mohsin.auth.android.components.TopAppBar
import com.mohsin.auth.android.core.navigation.NavigationViewModel
import com.mohsin.auth.android.core.navigation.Screen
import com.mohsin.auth.android.screens.add_account.AddAccountManually
import com.mohsin.auth.android.screens.add_account.QRScanView
import com.mohsin.auth.android.screens.dashboard.AddAccountOptionsView
import com.mohsin.auth.android.screens.dashboard.DashboardScreen
import com.mohsin.auth.android.screens.onboarding.OnboardingScreen
import com.mohsin.auth.android.screens.splash.SplashScreen
import com.mohsin.auth.android.utils.AccountOption
import com.mohsin.auth.android.utils.display
import com.mohsin.auth.domain.time.TotpTimer
import com.mohsin.auth.feature.AccountsViewModel
import com.mohsin.auth.feature.AddViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                MainScreen()
            }
        }
    }

    override fun onDestroy() {
        TotpTimer.unsubscribeAll()
        super.onDestroy()
    }
}

@Composable
fun MainScreen() {

    val snackBarHostState = remember { SnackbarHostState() }
    var isSnackBarVisible by remember { mutableStateOf(false) }

    val navigationViewModel: NavigationViewModel = viewModel()
    val topBarState = rememberSaveable { (mutableStateOf(false)) }
    val currentScreen = navigationViewModel.currentScreen

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val systemUiController = rememberSystemUiController()

    var showExitDialog by remember { mutableStateOf(false) }

    val activity = LocalActivity.current

    if (currentScreen == Screen.DashboardScreen) {
        BackHandler(enabled = currentScreen == Screen.DashboardScreen) {
            showExitDialog = true
        }
    }
    if (showExitDialog) {
        ConfirmationDialog(onAllow = {
            (activity as MainActivity).finish()
        }, onDismiss = {
            showExitDialog = false
        })
    }


        when (currentScreen) {
        Screen.SplashScreen -> {
            systemUiController.setStatusBarColor(
                color = Color.Transparent, darkIcons = true
            )
        }

        Screen.OnboardingScreen -> {
            systemUiController.setStatusBarColor(
                color = Color.Transparent, darkIcons = false
            )
        }

        Screen.DashboardScreen -> {
            systemUiController.setStatusBarColor(
                color = Color.Transparent, darkIcons = false
            )
        }

        else -> {}
    }

    Scaffold(modifier = Modifier.fillMaxSize(), snackbarHost = {
        SnackbarHost(hostState = snackBarHostState) { snackBarData ->
            AppSnackBar(snackBarData = snackBarData)
        }
    }, topBar = {
        if (topBarState.value) {
            TopAppBar(currentScreen = currentScreen, drawerState = drawerState, actions = {}, pop = {
                navigationViewModel.pop()
            })
        }
    }) { inset ->
        AppContent(
            navigationViewModel = navigationViewModel,
            modifier = Modifier.padding(inset),
            snackBarHostState,
            showToolbar = {
                topBarState.value = it
            }
        )
    }
}

@Composable
fun AppContent(
    navigationViewModel: NavigationViewModel,
    modifier: Modifier,
    snackBarHostState: SnackbarHostState,
    showToolbar: (Boolean) -> Unit
) {
    val scope = rememberCoroutineScope()
    val accountsViewModel: AccountsViewModel = koinViewModel()
    val addAccViewModel: AddViewModel = koinViewModel()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        addAccViewModel.success
            .flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.CREATED)
            .collect { account ->
                scope.launch(Dispatchers.Main) {
                    snackBarHostState.currentSnackbarData?.dismiss()
                    snackBarHostState.showSnackbar(
                        context.getString(
                            R.string.authentication_enabled_for,
                            account.name
                        ), duration = SnackbarDuration.Short)
                }
            }
    }

    LaunchedEffect(Unit) {
        addAccViewModel.error
            .flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.CREATED)
            .collect { error ->
                scope.launch(Dispatchers.Main) {
                    snackBarHostState.currentSnackbarData?.dismiss()
                    snackBarHostState.showSnackbar(error.display(context), duration = SnackbarDuration.Short)
                }
            }
    }

    NavDisplay(
        backStack = navigationViewModel.backStack, // Your custom-managed back stack
         transitionSpec = { // Define custom transitions for screen changes
            fadeIn(tween(300)) togetherWith fadeOut(tween(300))
        }, entryProvider = entryProvider { // Define your screen entries here
            entry<Screen.SplashScreen> {
                SplashScreen(navigateToNext = {
                    navigationViewModel.moveToScreen(Screen.OnboardingScreen)
                })
            }
            entry<Screen.OnboardingScreen> {
                OnboardingScreen(navigateToNext = {
                    navigationViewModel.moveToScreen(Screen.DashboardScreen)
                })
            }
            entry<Screen.DashboardScreen> {
                DashboardScreen(modifier,
                    showSnackbar = { message ->
                        scope.launch(Dispatchers.Main) {
                            snackBarHostState.currentSnackbarData?.dismiss()
                            snackBarHostState.showSnackbar(message, duration = SnackbarDuration.Short)
                        }
                    },
                    addViewModel = addAccViewModel,
                    accountsViewModel = accountsViewModel, showTopAppBar = {show -> showToolbar(show)},
                    showOptions = {
                        navigationViewModel.moveToScreen(Screen.AddAccountOptionsScreen)
                    },
                    navigateToManualAcc = {
                            navigationViewModel.moveToScreen(Screen.AddAccountManuallyScreen)
                    })
            }
            entry<Screen.AddAccountManuallyScreen> {
                AddAccountManually(modifier,
                    showSnackbar = {message ->
                        scope.launch(Dispatchers.Main) {
                            snackBarHostState.currentSnackbarData?.dismiss()
                            snackBarHostState.showSnackbar(message, duration = SnackbarDuration.Short)
                        }
                    },
                    accountCreated = {
                    navigationViewModel.popTo(Screen.DashboardScreen)
                })
            }
            entry<Screen.AddAccountOptionsScreen> {
                AddAccountOptionsView(modifier,
                    onQRResult = { scannedUri ->
                        addAccViewModel.createByUri(scannedUri)
                        navigationViewModel.pop()
                    },
                    navigateToManualAcc = {
                        navigationViewModel.moveToScreen(Screen.AddAccountManuallyScreen)
                    })
            }
            entry<Screen.QRScanScreen> {
                QRScanView(modifier = modifier, onResult = {})

            }

            /**
             *
             * For understanding
             */
            entry<Screen.ZoomBook>(
                // Entry for the ZoomBook screen, demonstrating object passing
                metadata = mapOf("extraDataKey" to "extraDataValue"), // Optional metadata
            ) { key -> // The 'key' argument provides type-safe access to passed objects
                /* ZoomBookInitScreen(x
                     book = key.book,
                 )*/
            }
        })
}