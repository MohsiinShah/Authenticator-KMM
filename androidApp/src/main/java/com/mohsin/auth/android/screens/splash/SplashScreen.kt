package com.mohsin.auth.android.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohsin.auth.android.R
import com.mohsin.auth.android.components.ActionButton
import com.mohsin.auth.android.components.TextContent

@Composable
fun SplashScreen(navigateToNext : () -> Unit) {

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(R.drawable.ic_splash), contentDescription = null
            )

            TextContent(
                value = stringResource(R.string.authenticator_app),
                textColor = colorResource(R.color.black),
                fontSize = 22.sp,
                fontFamily = FontFamily(
                    Font(R.font.readex_pro_medium)
                )
            )


            TextContent(
                modifier = Modifier.alpha(0.4f).padding(start = 20.dp, end = 20.dp, top =  10.dp),
                value = stringResource(R.string.splash_headline),
                textColor = colorResource(R.color.black),
                fontSize = 15.sp,
                fontFamily = FontFamily(
                    Font(R.font.readex_pro_light)
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            ActionButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 50.dp, start = 20.dp, end = 20.dp)
                    .clickable {
                        navigateToNext.invoke()
                    }, text = stringResource(R.string.get_started)
            )

        }


    }
}