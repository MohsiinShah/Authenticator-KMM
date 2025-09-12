package com.mohsin.auth.android.screens.dashboard

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mohsin.auth.android.R
import com.mohsin.auth.android.components.ActionButton
import com.mohsin.auth.android.components.ActionButtonBordered
import com.mohsin.auth.android.components.DeleteConfirmationDialog
import com.mohsin.auth.android.components.PermissionDialog
import com.mohsin.auth.android.components.TextContent
import com.mohsin.auth.domain.account.Account
import com.mohsin.auth.domain.account.HotpAccount
import com.mohsin.auth.domain.account.IntervalState
import com.mohsin.auth.domain.account.TotpAccount
import com.mohsin.auth.domain.otp.formatAsOtp
import com.mohsin.auth.domain.time.TotpListener
import com.mohsin.auth.domain.time.TotpTimer
import com.mohsin.auth.feature.AccountsViewModel
import com.mohsin.auth.feature.AddViewModel
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanQRCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DashboardScreen(
    modifier: Modifier,
    showSnackbar: (String) -> Unit,
    accountsViewModel: AccountsViewModel,
    addViewModel: AddViewModel,
    showTopAppBar: (Boolean) -> Unit,
    showOptions: () -> Unit,
    navigateToManualAcc: () -> Unit,
) {
    val context = LocalContext.current
    val accounts by accountsViewModel.accounts.collectAsStateWithLifecycle(minActiveState = Lifecycle.State.CREATED)

    LaunchedEffect(Unit) {
        showTopAppBar(true)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {

        if (accounts.isEmpty()) {
            AddAccountOptionsView(
                modifier = Modifier.align(Alignment.Center),
                onQRResult = { scannedUri ->
                    addViewModel.createByUri(scannedUri)
                },
                navigateToManualAcc = {
                    navigateToManualAcc()
                })
        }

        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.padding(top = 20.dp)) {
                items(accounts) { account ->
                    AccountView(
                        account = account, onCopy = {},
                        onDelete = { account ->
                            accountsViewModel.delete(account)
                        },
                        onHotpIncrement = { account ->
                            if (account != null) {
                                accountsViewModel.edit(account)
                            } else {
                                showSnackbar(context.getString(R.string.try_refreshing_in_a_few_seconds))
                            }
                        }
                    )
                }
            }
        }

        if (accounts.isNotEmpty()) {
            FloatingActionButton(
                modifier = Modifier
                    .padding(bottom = 40.dp, end = 20.dp)
                    .align(Alignment.BottomEnd),
                elevation = FloatingActionButtonDefaults.elevation(5.dp),
                containerColor = Color.Transparent, // removes background
                contentColor = Color.Unspecified,   // prevents tint
                onClick = {
                    showOptions()
                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_add_account),
                    contentDescription = "fab",
                )
            }
        }
    }
}

@Composable
fun AccountView(
    account: Account,
    onCopy: (String) -> Unit = {},
    onDelete: (Account) -> Unit = {},
    onHotpIncrement: (Account?) -> Unit = {},
) {
    val clipboardManager = LocalClipboardManager.current

    // Initialize with current values immediately
    var password by remember(account.name) {
        mutableStateOf(account.password.formatAsOtp())
    }
    var remainingTime by remember(account.name) {
        mutableStateOf(
            if (account is TotpAccount) account.secondsRemain().toString() else ""
        )
    }
    var progress by remember(account.name) {
        mutableStateOf(
            if (account is TotpAccount) {
                (account.secondsRemain() - 1).toFloat() / account.interval
            } else 0f
        )
    }
    var intervalState by remember(account.name) {
        mutableStateOf(
            if (account is TotpAccount) {
                val remain = account.secondsRemain()
                when {
                    remain > 20 -> IntervalState.HIGH
                    remain > 10 -> IntervalState.MEDIUM
                    else -> IntervalState.LOW
                }
            } else IntervalState.HIGH
        )
    }

    val scope = rememberCoroutineScope()

    // Optimize animation with reduced duration for smoother updates
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(
            durationMillis = 200, // Reduced from 500ms for more responsive updates
            easing = LinearEasing
        ),
        label = "progressAnim"
    )

    val hotpAnim = remember { Animatable(1f) }

    // Use remember to avoid recreating the listener on every recomposition
    val totpListener = remember(account.name) {
        if (account is TotpAccount) {
            object : TotpListener {
                private var lastSecond = -1 // Initialize to -1 to force first update

                override fun onTick() {
                    val remain = account.secondsRemain()

                    // Only update password when crossing into new second
                    if (remain != lastSecond) {
                        if (remain > lastSecond || lastSecond == -1) {
                            account.update()
                            password = account.password.formatAsOtp()
                        }

                        lastSecond = remain
                        remainingTime = remain.toString()

                        // Calculate progress more efficiently
                        progress = (remain - 1).coerceAtLeast(0).toFloat() / account.interval

                        // Update interval state
                        intervalState = when {
                            remain > 20 -> IntervalState.HIGH
                            remain > 10 -> IntervalState.MEDIUM
                            else -> IntervalState.LOW
                        }
                    }
                }
            }
        } else null
    }

    // Optimize DisposableEffect to only run when account changes
    DisposableEffect(account.name) {
        totpListener?.let { listener ->
            TotpTimer.subscribe(listener)
            // Trigger immediate update to sync with timer
            listener.onTick()
        }

        onDispose {
            totpListener?.let { listener ->
                TotpTimer.unsubscribe(listener)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, bottom = 8.dp)
            .wrapContentHeight()
            .border(
                width = 1.dp,
                color = colorResource(R.color.border_grey),
                shape = RoundedCornerShape(corner = CornerSize(14.dp))
            )
            .clip(RoundedCornerShape(corner = CornerSize(14.dp)))
            .clickable {
                clipboardManager.setText(AnnotatedString(text = account.password))
            }
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    when (account) {
                        is TotpAccount -> {
                            TimerCircularProgress(
                                animatedProgress = animatedProgress,
                                remainingTime = remainingTime,
                                intervalState = intervalState
                            )
                        }

                        is HotpAccount -> {
                            HotpCircularProgress(hotpAnim = hotpAnim)
                        }
                    }

                    AccountInfoColumn(account = account)
                    Spacer(modifier = Modifier.weight(1f))
                }

                PasswordRow(
                    password = password,
                    account = account,
                    hotpAnim = hotpAnim,
                    scope = scope,
                    onHotpIncrement = onHotpIncrement,
                    onPasswordUpdate = { newPassword -> password = newPassword }
                )
            }

            ActionMenuColumn(
                onDelete = {
                    onDelete(account)
                },
                onCopy = {
                    clipboardManager.setText(AnnotatedString(text = account.password))
                })
        }
    }
}

@Composable
private fun TimerCircularProgress(
    animatedProgress: Float,
    remainingTime: String,
    intervalState: IntervalState,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                progress = { animatedProgress },
                strokeWidth = 4.dp,
                color = when (intervalState) {
                    IntervalState.HIGH -> colorResource(R.color.primary_blue)
                    IntervalState.MEDIUM -> colorResource(R.color.medium_yellow)
                    IntervalState.LOW -> colorResource(R.color.low_red)
                },
                trackColor = colorResource(R.color.border_grey),
                modifier = Modifier.size(60.dp)
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .alpha(if (remainingTime.isNotEmpty()) 1f else 0f)
                    .size(46.dp)
                    .background(
                        color = when (intervalState) {
                            IntervalState.HIGH -> colorResource(R.color.primary_blue)
                            IntervalState.MEDIUM -> colorResource(R.color.medium_yellow)
                            IntervalState.LOW -> colorResource(R.color.low_red)
                        },
                        shape = RoundedCornerShape(50)
                    )
            ) {
                Text(
                    text = remainingTime,
                    color = colorResource(R.color.white),
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.readex_pro_semi_bold))
                )
            }
        }
    }
}

@Composable
private fun HotpCircularProgress(hotpAnim: Animatable<Float, AnimationVector1D>) {
    Box(contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            progress = { hotpAnim.value },
            strokeWidth = 4.dp,
            color = colorResource(R.color.primary_blue),
            trackColor = colorResource(R.color.border_grey),
            modifier = Modifier.size(60.dp)
        )

        Image(
            painter = painterResource(R.drawable.ic_account),
            contentDescription = null
        )
    }
}

@Composable
private fun AccountInfoColumn(account: Account) {
    Column(
        modifier = Modifier
            .height(60.dp)
            .padding(start = 8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        TextContent(
            Modifier.fillMaxWidth(0.8f),
            value = account.issuer ?: stringResource(R.string.account),
            textColor = colorResource(R.color.primary_blue),
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.readex_pro_semi_bold))
        )

        TextContent(
            modifier = Modifier.fillMaxWidth(0.8f),
            value = account.name,
            textColor = colorResource(R.color.black),
            fontSize = 12.sp,
            fontFamily = FontFamily(Font(R.font.readex_pro_regular))
        )
    }
}

@Composable
private fun PasswordRow(
    password: String,
    account: Account,
    hotpAnim: Animatable<Float, AnimationVector1D>,
    scope: CoroutineScope,
    onHotpIncrement: (Account?) -> Unit,
    onPasswordUpdate: (String) -> Unit,
) {
    Row(
        modifier = Modifier.padding(start = 65.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Use key() to prevent unnecessary recompositions of digits that haven't changed
        password.forEachIndexed { index, digit ->
            key("${account.name}_digit_$index") {
                AnimatedDigitView(digit = digit, index = index)
            }
        }

        if (account is HotpAccount) {
            IconButton(
                onClick = {
                    if (account.increment()) {
                        val newPassword = account.password.formatAsOtp()
                        onPasswordUpdate(newPassword)
                        scope.launch {
                            hotpAnim.snapTo(0f)
                            hotpAnim.animateTo(
                                targetValue = 1f,
                                animationSpec = tween(
                                    durationMillis = 2000,
                                    easing = LinearEasing
                                )
                            )
                        }
                        onHotpIncrement(account)
                    } else {
                        onHotpIncrement(null)
                    }
                }
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_refresh),
                    contentDescription = null
                )
            }
        }
    }
}


@Composable
private fun ActionMenuColumn(
    onCopy: () -> Unit = {},
    onDelete: () -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    if(showDeleteConfirmation){
        DeleteConfirmationDialog(onAllow = {
            onDelete()
        }, onDismiss = {
            showDeleteConfirmation = false
        })
    }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                painter = painterResource(R.drawable.ic_action_button),
                contentDescription = "More options"
            )
        }

        DropdownMenu(
            containerColor = Color.White,
            border = BorderStroke(1.dp, color = colorResource(R.color.drop_down_border)),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                leadingIcon = {
                    Icon(painter = painterResource(R.drawable.ic_copy), contentDescription = null)
                },
                text = {
                    TextContent(
                        modifier = Modifier,
                        value = "Copy",
                        textColor = colorResource(R.color.black),
                        fontSize = 12.sp,
                        fontFamily = FontFamily(
                            Font(R.font.readex_pro_regular)
                        )
                    )
                },
                onClick = {
                    onCopy()
                    expanded = false
                }
            )

            HorizontalDivider(
                color = colorResource(R.color.drop_down_border),
                modifier = Modifier.weight(1f)
            )

            DropdownMenuItem(
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Delete, contentDescription = null)
                },
                text = {
                    TextContent(
                        modifier = Modifier,
                        value = "Delete",
                        textColor = colorResource(R.color.black),
                        fontSize = 12.sp,
                        fontFamily = FontFamily(
                            Font(R.font.readex_pro_regular)
                        )
                    )

                },
                onClick = {
                    showDeleteConfirmation = true
                    expanded = false
                }
            )
        }
    }
}


@Composable
fun OptimizedAnimatedDigitView(digit: Char, index: Int) {
    val scale = remember { Animatable(1f) } // Start at normal size
    val alpha = remember { Animatable(1f) }  // Start fully visible
    val offsetY = remember { Animatable(0f) } // Start at normal position

    // Only animate when digit actually changes
    LaunchedEffect(digit) {
        // Quick scale/fade animation
        launch {
            scale.animateTo(
                targetValue = 1.1f,
                animationSpec = tween(durationMillis = 100, easing = LinearEasing)
            )
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 100, easing = LinearEasing)
            )
        }
    }

    TextContent(
        modifier = Modifier
            .scale(scale.value)
            .alpha(alpha.value)
            .offset(y = offsetY.value.dp),
        value = digit.toString(),
        textColor = colorResource(R.color.primary_blue),
        fontSize = 35.sp,
        fontFamily = FontFamily(Font(R.font.readex_pro_regular))
    )
}

@Composable
fun AnimatedDigitView(digit: Char, index: Int) {
    val scale = remember { Animatable(0.5f) }
    val alpha = remember { Animatable(0f) }
    val offsetY = remember { Animatable(20f) }

    LaunchedEffect(digit) {
        scale.snapTo(0.5f)
        alpha.snapTo(0f)
        offsetY.snapTo(20f)
        delay(index * 50L) // Staggered delay for each digit
        launch {
            scale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = 0.6f, // Slight bounce
                    stiffness = 1000f
                )
            )
        }
        launch {
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 100,
                    easing = LinearEasing
                )
            )
        }
        launch {
            offsetY.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = 100,
                    easing = LinearEasing
                )
            )
        }
    }

    TextContent(
        modifier = Modifier
            .scale(scale.value)
            .alpha(alpha.value)
            .offset(y = offsetY.value.dp),
        value = digit.toString(),
        textColor = colorResource(R.color.primary_blue),
        fontSize = 35.sp,
        fontFamily = FontFamily(Font(R.font.readex_pro_regular))
    )
}


@Preview
@Composable
fun AddAccountOptionsView(
    modifier: Modifier = Modifier,
    onQRResult: (String) -> Unit = {},
    navigateToManualAcc: () -> Unit = {},
) {

    var checkPermissionsAndMove by remember { mutableStateOf(false) }


    val qrLauncher = rememberLauncherForActivityResult(ScanQRCode()) { result: QRResult ->
        when (result) {
            is QRResult.QRSuccess -> onQRResult(result.content.rawValue ?: "")
            else -> {
                checkPermissionsAndMove = false
            }
        }
    }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.dashboard))
    val progress by animateLottieCompositionAsState(
        composition = composition, iterations = LottieConstants.IterateForever
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.white)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        if (checkPermissionsAndMove) {
            CameraPermissionHandler(
                onGranted = { qrLauncher.launch(null) },
                resetState = {checkPermissionsAndMove = false}
            )
        }

        LottieAnimation(
            composition = composition, progress = { progress },
            modifier = Modifier
                .width(145.dp)
                .height(130.dp))

        Image(
            painter = painterResource(R.drawable.ic_first_2fa),
            contentDescription = null,
            modifier = Modifier.padding(top = 16.dp)
        )

        TextContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp, end = 40.dp, top = 10.dp),
            value = stringResource(R.string.dashboard_desc),
            textColor = colorResource(R.color.black_60),
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(
                Font(R.font.readex_pro_light)
            )
        )

        ActionButton(
            modifier = Modifier
                .padding(top = 20.dp)
                .width(200.dp)
                .clickable {
                    checkPermissionsAndMove = true
                }, text = stringResource(R.string.scan_qr_code)
        )

        ActionButtonBordered(
            modifier = Modifier
                .padding(top = 20.dp)
                .width(200.dp)
                .clickable {
                    navigateToManualAcc()
                }, text = stringResource(R.string.enter_manually)
        )

    }
}

@Composable
fun CameraPermissionHandler(
    onGranted: () -> Unit,
    onDenied: () -> Unit = {},
    onPermanentDenied: () -> Unit = {},
    resetState : () -> Unit = {}
) {
    val context = LocalContext.current
    val activity = context as? Activity

    var showRationaleDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onGranted()
        } else {
            if (activity?.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) == true) {
                showRationaleDialog = true
                onDenied()
            } else {
                showSettingsDialog = true
                onPermanentDenied()
            }
        }
    }

    // Launch the permission request when this composable is used
    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    if (showRationaleDialog) {
        PermissionDialog(
            onAllow = {
                showRationaleDialog = false
                permissionLauncher.launch(Manifest.permission.CAMERA)
            },
            onDismiss = {
                showRationaleDialog = false
                resetState()
            }
        )
    }

    if (showSettingsDialog) {
        PermissionDialog(
            positiveButtonText = stringResource(R.string.go_to_settings),
            description = stringResource(R.string.camera_permission_denied_permanently),
            onAllow = {
                showSettingsDialog = false
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
                activity?.startActivity(intent)
                resetState()
            },
            onDismiss = {
                showSettingsDialog = false
                resetState()
            }
        )
    }
}



