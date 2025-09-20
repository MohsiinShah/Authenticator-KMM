package com.mohsin.auth.android.components

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohsin.auth.android.R

@Composable
fun TextContent(
    modifier: Modifier = Modifier,
    value: String,
    fontSize: TextUnit = 12.sp,
    fontFamily: FontFamily,
    enableMarquee: Boolean = false,
    textAlign: TextAlign = TextAlign.Start,
    textColor: Color = colorResource(R.color.black),
    textDecoration: TextDecoration = TextDecoration.None,
    style: TextStyle = TextStyle.Default,
) {
    if (enableMarquee) {
        modifier.basicMarquee(iterations = Int.MAX_VALUE)
    }
    val baseStyle = style.merge(
        TextStyle(
            platformStyle = PlatformTextStyle(includeFontPadding = false)
        )
    )

    Text(
        text = value,
        color = textColor,
        fontFamily = fontFamily,
        style = baseStyle,
        fontSize = fontSize,
        modifier = modifier,
        textAlign = textAlign,
        textDecoration = textDecoration
    )
}

@Composable
fun RoundedBorderedTextField(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    hint: String = "Enter text",
    isPassword: Boolean = false,
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = colorResource(R.color.border_grey),
                shape = RoundedCornerShape(12.dp)
            ),
        placeholder = {
            Text(
                text = hint, color = colorResource(R.color.black_30),
                fontFamily = FontFamily(Font(R.font.readex_pro_light))
            )
        },
        textStyle = TextStyle(
            fontSize = 16.sp,
            color = colorResource(R.color.black),
            fontFamily = FontFamily(Font(R.font.readex_pro_light))
        ),
        singleLine = false,
        maxLines = 4,
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (isPassword && !passwordVisible) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        trailingIcon = {
            if (isPassword) {
                val image = if (passwordVisible)
                    painterResource(R.drawable.ic_password_visible) // 👁 your open eye icon
                else
                    painterResource(R.drawable.ic_password_invisible) // 👁‍🗨 your closed eye icon

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = image,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,
            errorBorderColor = Color.Transparent,
            cursorColor = colorResource(R.color.primary_blue)
        )
    )
}
