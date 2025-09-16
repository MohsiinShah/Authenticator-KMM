package com.mohsin.auth.android

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
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
import com.mohsin.auth.android.components.TextContent
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
import com.mohsin.auth.domain.Constants
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

    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    var isSnackBarVisible by remember { mutableStateOf(false) }

    val navigationViewModel: NavigationViewModel = viewModel()
    val topBarState = rememberSaveable { (mutableStateOf(false)) }
    val currentScreen = navigationViewModel.currentScreen

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val systemUiController = rememberSystemUiController()

    var showExitDialog by remember { mutableStateOf(false) }

    val activity = LocalActivity.current

    val isDrawerGestureEnabled by remember(currentScreen) {
        derivedStateOf { currentScreen == Screen.DashboardScreen }
    }

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

    ModalNavigationDrawer(
        modifier = Modifier,
        drawerState = drawerState,
        gesturesEnabled = isDrawerGestureEnabled,
        drawerContent = {
            AppDrawer(
                context = context,
                drawerState = drawerState,
                snackBarHostState = snackBarHostState,
                dispatchFirebaseEvent = { eventName ->
                },
                onExitClick = {
                    showExitDialog = true
                }
            )
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize(), snackbarHost = {
            SnackbarHost(hostState = snackBarHostState) { snackBarData ->
                AppSnackBar(snackBarData = snackBarData)
            }
        }, topBar = {
            if (topBarState.value) {
                TopAppBar(
                    currentScreen = currentScreen,
                    drawerState = drawerState,
                    actions = {},
                    pop = {
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

@Composable
fun AppDrawer(
    drawerState: DrawerState,
    snackBarHostState: SnackbarHostState,
    context: Context,
    dispatchFirebaseEvent: (String) -> Unit,
    onExitClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val noSupportedAppMsg = stringResource(R.string.no_supported_app_msg)

    ModalDrawerSheet(
        windowInsets = WindowInsets.navigationBars,
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        drawerContainerColor = colorResource(R.color.white)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
                .background(color = colorResource(R.color.white))
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(150.dp)
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(0.8f),
                    painter = painterResource(R.drawable.ic_splash),
                    tint = androidx.compose.ui.graphics.Color.Unspecified,
                    contentDescription = null
                )
            }

            TextContent(
                value = stringResource(R.string.authenticator_app),
                textColor = colorResource(R.color.black),
                fontSize = 15.sp,
                fontFamily = FontFamily(Font(R.font.readex_pro_semi_bold))
            )

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider(color = colorResource(R.color.border_grey),
                modifier = Modifier.fillMaxSize(0.8f))
        }


        listOf(
            R.drawable.ic_privacy_policy to R.string.privacy_policy,
            R.drawable.ic_exit_drawer to R.string.exit
        ).forEachIndexed { _, (iconId, labelRes) ->
            DrawerItem(
                iconId = iconId,
                label = stringResource(labelRes),
                onClick = {
                    coroutineScope.launch { drawerState.close() }
                    when (labelRes) {
                        R.string.privacy_policy -> {
                            //logging firebase event
                            //generating intent
                            val urlIntent = Intent(Intent.ACTION_VIEW).apply {
                                data = Constants.PRIVACY_POLICY.toUri()
                            }

                            try {
                                context.startActivity(urlIntent)
                            } catch (e: ActivityNotFoundException) {
                                coroutineScope.launch {
                                    snackBarHostState.currentSnackbarData?.dismiss()
                                    snackBarHostState.showSnackbar(message = noSupportedAppMsg)
                                }
                            }
                        }
                    }
                },
                onExitClick = {
                    coroutineScope.launch { drawerState.close() }
                    onExitClick.invoke()
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        TextContent(
            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
            value = "v-${BuildConfig.APP_VERSION_NAME}",
            textColor = colorResource(R.color.black),
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.readex_pro_regular))
        )
    }
}

@Composable
fun DrawerItem(
    iconId: Int,
    label: String,
    onClick: () -> Unit,
    onExitClick: () -> Unit
) {
    NavigationDrawerItem(
        modifier = Modifier,
        icon = {
            Icon(
                modifier = Modifier
                    .size(40.dp)
                    .padding(8.dp),
                painter = painterResource(iconId),
                contentDescription = label
            )
        },
        label = {

            TextContent(
                value = label,
                textColor = colorResource(R.color.black),
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.readex_pro_regular))
            )
        },
        selected = false,
        onClick = if(label == stringResource(R.string.exit)) onExitClick else onClick
    )
}