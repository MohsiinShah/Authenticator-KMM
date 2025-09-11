package com.mohsin.auth.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohsin.auth.android.R

@Preview
@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    height: Dp = 52.dp,
    textSize: TextUnit = 17.sp,
    fontFamily: FontFamily = FontFamily(
        Font(R.font.readex_pro_medium)
    ),
    text: String = "",
) {

    Box(
        modifier = modifier
            .height(height)
            .clip(RoundedCornerShape(10.dp))
            .background(color = colorResource(R.color.primary_blue))
    ) {
        TextContent(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center),
            value = text,
            fontSize = textSize,
            textAlign = TextAlign.Center,
            fontFamily = fontFamily,
            textColor = colorResource(R.color.white)
        )
    }
}

@Preview
@Composable
fun ActionButtonBordered(
    modifier: Modifier = Modifier,
    height: Dp = 52.dp,
    textSize: TextUnit = 17.sp,
    fontFamily: FontFamily = FontFamily(
        Font(R.font.readex_pro_medium)
    ),
    text: String = "",
) {

    Box(
        modifier = modifier
            .height(height)
            .border(width = 1.dp, color = colorResource(R.color.primary_blue),
                shape = RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(color = colorResource(R.color.white))
    ) {
        TextContent(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center),
            value = text,
            fontSize = textSize,
            textAlign = TextAlign.Center,
            fontFamily = fontFamily,
            textColor = colorResource(R.color.primary_blue)
        )
    }
}