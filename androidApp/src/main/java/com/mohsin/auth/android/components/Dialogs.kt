package com.mohsin.auth.android.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.mohsin.auth.android.R

@Composable
fun PermissionDialog(
    @DrawableRes icon: Int = R.drawable.ic_permission,
    title: String = stringResource(R.string.permission),
    description: String = stringResource(R.string.permission_rationale),
    positiveButtonText: String = stringResource(R.string.allow),
    onAllow: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
        Dialog(onDismissRequest = { onDismiss() }) {
            // Card for the dialog UI
            Card(
                colors = CardDefaults.cardColors(containerColor = colorResource(R.color.white)),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Box {

                    Image(
                        modifier = Modifier.alpha(0.6f),
                        painter = painterResource(R.drawable.ic_top),
                        contentDescription = null)

                    Column(
                        modifier = Modifier
                            .padding(start = 40.dp, end = 40.dp, top = 40.dp, bottom = 20.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Image(painter = painterResource(id = icon), contentDescription = null,
                            modifier = Modifier
                                .size(80.dp))

                        Spacer(modifier = Modifier.height(10.dp))

                        TextContent(
                            modifier = Modifier
                                .fillMaxWidth(),
                            value = title,
                            textColor = colorResource(R.color.black),
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily(
                                Font(R.font.readex_pro_regular)
                            )
                        )

                        TextContent(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .fillMaxWidth(),
                            value = description,
                            textColor = colorResource(R.color.black_80),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily(
                                Font(R.font.readex_pro_light)
                            )
                        )

                        ActionButton(
                            modifier = Modifier
                                .padding(top = 20.dp)
                                .fillMaxWidth()
                                .clickable {
                                    onAllow()
                                }, text = positiveButtonText
                        )

                        ActionButtonBordered(
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .fillMaxWidth()
                                .clickable {
                                    onDismiss()
                                }, text = stringResource(R.string.cancel)
                        )
                    }
                }
            }

    }
}

@Preview(showBackground = true)
@Composable
fun ConfirmationDialog(
    @DrawableRes icon: Int = R.drawable.ic_exit,
    title: String = stringResource(R.string.exit),
    description: String = stringResource(R.string.are_you_sure_you_want_to_exit),
    onAllow: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
        Dialog(onDismissRequest = { onDismiss() }) {
            // Card for the dialog UI
            Card(
                colors = CardDefaults.cardColors(containerColor = colorResource(R.color.white)),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Box {

                    Image(
                        modifier = Modifier.alpha(0.6f),
                        painter = painterResource(R.drawable.ic_top),
                        contentDescription = null)

                    Column(
                        modifier = Modifier
                            .padding(start = 40.dp, end = 40.dp, top = 40.dp, bottom = 20.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Image(painter = painterResource(id = icon), contentDescription = null,
                            modifier = Modifier
                                .size(80.dp))

                        Spacer(modifier = Modifier.height(10.dp))

                        TextContent(
                            modifier = Modifier
                                .fillMaxWidth(),
                            value = title,
                            textColor = colorResource(R.color.black),
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily(
                                Font(R.font.readex_pro_regular)
                            )
                        )

                        TextContent(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .fillMaxWidth(),
                            value = description,
                            textColor = colorResource(R.color.black_80),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily(
                                Font(R.font.readex_pro_light)
                            )
                        )

                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp)) {
                            ActionButton(

                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        onAllow()
                                    }, text = stringResource(R.string.yes)
                            )

                            Spacer(modifier = Modifier.width(20.dp))

                            ActionButtonBordered(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        onDismiss()
                                    }, text = stringResource(R.string.cancel)
                            )
                        }
                    }
                }
            }
        }
}

@Preview(showBackground = true)
@Composable
fun DeleteConfirmationDialog(
    @DrawableRes icon: Int = R.drawable.ic_exit,
    title: String = stringResource(R.string.delete_account),
    description: String = stringResource(R.string.delete_desc),
    onAllow: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        // Card for the dialog UI
        Card(
            colors = CardDefaults.cardColors(containerColor = colorResource(R.color.white)),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Box {
                Column(
                    modifier = Modifier
                        .padding(start = 40.dp, end = 40.dp, top = 40.dp, bottom = 20.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

//                    Image(painter = painterResource(id = icon), contentDescription = null,
//                        modifier = Modifier
//                            .size(80.dp))

                    Spacer(modifier = Modifier.height(10.dp))

                    TextContent(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = title,
                        textColor = colorResource(R.color.black),
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(
                            Font(R.font.readex_pro_regular)
                        )
                    )

                    TextContent(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth(),
                        value = description,
                        textColor = colorResource(R.color.black_80),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(
                            Font(R.font.readex_pro_light)
                        )
                    )

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)) {
                        ActionButton(
                            colorRes = colorResource(R.color.low_red),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    onAllow()
                                }, text = stringResource(R.string.yes)
                        )

                        Spacer(modifier = Modifier.width(20.dp))

                        ActionButtonBordered(
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    onDismiss()
                                }, text = stringResource(R.string.no)
                        )
                    }
                }
            }
        }
    }

}