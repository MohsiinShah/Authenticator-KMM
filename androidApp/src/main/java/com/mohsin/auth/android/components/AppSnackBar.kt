package com.mohsin.auth.android.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohsin.auth.android.R

@Composable
fun AppSnackBar(snackBarData: SnackbarData) {
    Snackbar(
        modifier = Modifier
            .safeDrawingPadding()
            .padding(horizontal = 15.dp, vertical = 10.dp),
        contentColor = colorResource(R.color.black),
        containerColor = colorResource(R.color.white),
        actionOnNewLine = true,
        action = {
            snackBarData.visuals.actionLabel?.let { actionLabel ->
                TextButton(onClick = { snackBarData.performAction() }) {
                    TextContent(
                        textColor = colorResource(R.color.black),
                        value = actionLabel.uppercase(),
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(
                            Font(R.font.readex_pro_medium)
                        )
                    )
                }
            }
        }
    ) {
        TextContent(
            textColor = colorResource(R.color.black),
            value = snackBarData.visuals.message,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(
                Font(R.font.readex_pro_medium)
            )
        )
    }
}
