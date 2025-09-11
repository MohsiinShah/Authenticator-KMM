package com.mohsin.auth.android.screens.add_account

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import com.mohsin.auth.android.R
import com.mohsin.auth.android.components.ActionButton
import com.mohsin.auth.android.components.RoundedBorderedTextField
import com.mohsin.auth.android.components.TextContent
import com.mohsin.auth.feature.AddViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.lang.Exception

@Preview
@Composable
fun AddAccountManually(modifier: Modifier = Modifier,
                       showSnackbar: (String) -> Unit = {},
                        addAccViewModel: AddViewModel = koinViewModel(),
                       accountCreated: () -> Unit = {}){

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var accountName by remember { mutableStateOf("") }

    var secretKey by remember { mutableStateOf("") }

    var checkTOTP by remember { mutableStateOf(true) }

    var checkHOTP by remember { mutableStateOf(false) }

    var additionalOptions by remember { mutableStateOf(false) }

    var intervalTOTP by remember { mutableStateOf("30") }

    var counterHOTP by remember { mutableStateOf("0") }

    var showPopupAccountName by remember { mutableStateOf(false) }
    var showPopupSecretKey by remember { mutableStateOf(false) }
    var showPopupAlgo by remember { mutableStateOf(false) }
    var showPopupTOTP by remember { mutableStateOf(false) }
    var showPopupHOTP by remember { mutableStateOf(false) }




    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        addAccViewModel.success
            .flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.CREATED)
            .collect { account ->
                accountCreated()
            }
    }

    Box(modifier = modifier
        .fillMaxSize()
        .background(Color.White)){

        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 30.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 10.dp)) {

                TextContent(
                    value = stringResource(R.string.account_name),
                    textColor = colorResource(R.color.black),
                    fontSize = 14.sp,
                    fontFamily = FontFamily(
                        Font(R.font.readex_pro_regular)
                    )
                )

                Box{
                IconButton(
                    onClick = {
                        showPopupAccountName = true
                    },
                    modifier = Modifier
                        .size(24.dp)
                        .padding(start = 10.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.info),
                        contentDescription = "Info",
                        modifier = Modifier.size(24.dp)
                    )
                }

                    if (showPopupAccountName) {
                        InfoBubbleAboveRight(
                            text = stringResource(R.string.account_info),
                            onDismiss = { showPopupAccountName = false }
                        )
                    }
                }
            }

            RoundedBorderedTextField(
                modifier = Modifier.padding(top = 10.dp),
                value = accountName,
                onValueChange = { accountName = it },
                hint = "Account name"
            )

            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 10.dp)) {

                TextContent(
                    value = stringResource(R.string.secret_key),
                    textColor = colorResource(R.color.black),
                    fontSize = 14.sp,
                    fontFamily = FontFamily(
                        Font(R.font.readex_pro_regular)
                    )
                )
                Box{
                    IconButton(
                        onClick = {
                            showPopupSecretKey = true
                        },
                        modifier = Modifier
                            .size(24.dp)
                            .padding(start = 10.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.info),
                            contentDescription = "Info",
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    if (showPopupSecretKey) {
                        InfoBubbleAboveRight(
                            text = stringResource(R.string.secret_info),
                            onDismiss = { showPopupSecretKey = false }
                        )
                    }
                }
            }

            RoundedBorderedTextField(
                modifier = Modifier.padding(top = 10.dp),
                value = secretKey,
                onValueChange = { secretKey = it },
                hint = "Secret key",
                isPassword = true
            )

            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 10.dp)) {
                TextContent(
                    value = stringResource(R.string.algorithm),
                    textColor = colorResource(R.color.black),
                    fontSize = 14.sp,
                    fontFamily = FontFamily(
                        Font(R.font.readex_pro_regular)
                    )
                )

                Box{
                    IconButton(
                        onClick = {
                            showPopupAlgo = true
                        },
                        modifier = Modifier
                            .size(24.dp)
                            .padding(start = 10.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.info),
                            contentDescription = "Info",
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    if (showPopupAlgo) {
                        InfoBubbleAboveRight(
                            text = stringResource(R.string.algo_info) +
                                    "HOTP (Counter-based One-Time Password)",
                            onDismiss = { showPopupAlgo = false }
                        )
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically,) {
                Checkbox(
                    checked = checkTOTP,
                    onCheckedChange = {
                        checkTOTP = it
                        if(it){
                            checkHOTP = false
                        }
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = colorResource(R.color.primary_blue),        // Fill when checked
                        uncheckedColor = Color.LightGray,             // Border when unchecked
                        checkmarkColor = Color.White,            // The tick color
                        disabledCheckedColor = Color.LightGray,  // When disabled + checked
                        disabledUncheckedColor = Color.LightGray // When disabled + unchecked
                    )
                )

                TextContent(
                    modifier = Modifier,
                    value = stringResource(R.string.by_time_totp),
                    textColor = colorResource(R.color.black),
                    fontSize = 12.sp,
                    fontFamily = FontFamily(
                        Font(R.font.readex_pro_light)
                    )
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically,) {
                Checkbox(
                    checked = checkHOTP,
                    onCheckedChange = {
                        checkHOTP = it
                        if(it){
                            checkTOTP = false
                        }
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = colorResource(R.color.primary_blue),        // Fill when checked
                        uncheckedColor = Color.LightGray,             // Border when unchecked
                        checkmarkColor = Color.White,            // The tick color
                        disabledCheckedColor = Color.LightGray,  // When disabled + checked
                        disabledUncheckedColor = Color.LightGray // When disabled + unchecked
                    )
                )

                TextContent(
                    modifier = Modifier,
                    value = stringResource(R.string.by_counter_hotp),
                    textColor = colorResource(R.color.black),
                    fontSize = 12.sp,
                    fontFamily = FontFamily(
                        Font(R.font.readex_pro_light)
                    )
                )
            }

/*            Row(verticalAlignment = Alignment.CenterVertically,) {
                TextContent(
                    modifier = Modifier,
                    value = "Show additional options",
                    textColor = colorResource(R.color.black),
                    fontSize = 12.sp,
                    fontFamily = FontFamily(
                        Font(R.font.readex_pro_regular)
                    )
                )

                Switch(
                    modifier = Modifier.padding(start = 16.dp),
                    checked = additionalOptions,
                    onCheckedChange = { additionalOptions = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = colorResource(R.color.primary_blue),          // Thumb color when ON
                        checkedTrackColor = colorResource(R.color.border_grey),   // Track color when ON
                        uncheckedThumbColor = Color.Gray,       // Thumb color when OFF
                        uncheckedTrackColor = colorResource(R.color.border_grey),        // Track color when OFF
                        checkedBorderColor = colorResource(R.color.border_grey),        // Border when ON
                        uncheckedBorderColor = colorResource(R.color.border_grey)    // Border when OFF
                    )
                )
            }*/

            if(additionalOptions){
                if(checkTOTP) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TextContent(
                            modifier = Modifier,
                            value = "Refresh Interval",
                            textColor = colorResource(R.color.black),
                            fontSize = 12.sp,
                            fontFamily = FontFamily(
                                Font(R.font.readex_pro_light)
                            )
                        )

                        Box{
                            IconButton(
                                onClick = {
                                    showPopupTOTP = true
                                },
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(start = 10.dp)
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.info),
                                    contentDescription = "Info",
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            if (showPopupTOTP) {
                                InfoBubbleAboveRight(
                                    text = stringResource(R.string.totp_info),
                                    onDismiss = { showPopupTOTP = false }
                                )
                            }
                        }
                    }

                    RoundedBorderedTextField(
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .fillMaxWidth(0.4f),
                        value = intervalTOTP,
                        onValueChange = { intervalTOTP = it },
                    )
                }else{
                    Row(verticalAlignment = Alignment.CenterVertically) {

                    TextContent(
                        modifier = Modifier,
                        value = "Initial Counter",
                        textColor = colorResource(R.color.black),
                        fontSize = 12.sp,
                        fontFamily = FontFamily(
                            Font(R.font.readex_pro_light)
                        )
                    )


                        Box{
                            IconButton(
                                onClick = {
                                    showPopupTOTP = true
                                },
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(start = 10.dp)
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.info),
                                    contentDescription = "Info",
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            if (showPopupTOTP) {
                                InfoBubbleAboveRight(
                                    text = stringResource(R.string.hotp_info),
                                    onDismiss = { showPopupTOTP = false }
                                )
                            }
                        }
                    }

                    RoundedBorderedTextField(
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .fillMaxWidth(0.4f),
                        value = counterHOTP,
                        onValueChange = { counterHOTP = it },
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {

                ActionButton(
                    modifier = Modifier
                        .width(200.dp)
                        .clickable {
                            if (checkTOTP) {
                                addAccViewModel.createTotp(
                                    name = accountName,
                                    secret = secretKey,
                                    interval = intervalTOTP.toLong()
                                )
                            } else {
                                addAccViewModel.createHotp(
                                    name = accountName,
                                    secret = secretKey,
                                    counter = counterHOTP.toLong()
                                )
                            }

                        }, text = "Create Account"
                )
            }
        }
    }
}

@Composable
fun InfoBubbleAboveRight(
    text: String,
    onDismiss: () -> Unit
) {
    Popup(
        alignment = Alignment.TopStart,
        onDismissRequest = { onDismiss() },
        properties = PopupProperties(
            usePlatformDefaultWidth = false,
            clippingEnabled = false
        )
    ) {
        Surface(
            modifier = Modifier
                .wrapContentSize()
                .widthIn(max = 200.dp)
                .padding(4.dp), // Space for border and shadow
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color.LightGray),
            color = Color.White,
            shadowElevation = 4.dp
        ) {
            Text(
                text = text,
                color = Color.Black,
                fontSize = 12.sp,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .fillMaxWidth()
            )
        }
    }
}


