package com.mohsin.auth.android.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohsin.auth.android.R
import com.mohsin.auth.android.core.navigation.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(currentScreen: Screen, drawerState: DrawerState, actions: @Composable RowScope.() -> Unit = {}, pop: () -> Unit
){
    val scope = rememberCoroutineScope()

    TopAppBar(
        modifier = Modifier.fillMaxWidth(), colors = TopAppBarColors(
            containerColor = colorResource(R.color.primary_blue),
            scrolledContainerColor = Color.Transparent,
            navigationIconContentColor = Color.Unspecified,
            titleContentColor = colorResource(R.color.white),
            actionIconContentColor = Color.Unspecified
        ), title = {
            val title = if(currentScreen == Screen.AddAccountManuallyScreen){
                stringResource(R.string.new_account)
            }else if(currentScreen == Screen.QRScanScreen) {
                stringResource(R.string.scan_qr)
            } else{
                stringResource(R.string.authenticator)
            }

            TextContent(
                value = title,
                textColor = colorResource(R.color.white),
                fontSize = 20.sp,
                fontFamily = FontFamily(
                    Font(R.font.readex_pro_medium)
                )
            )
        }, actions = actions, navigationIcon = {
            when(currentScreen){
                Screen.DashboardScreen -> {
                    IconButton(
                        onClick = {
                            scope.launch { drawerState.open() }
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            painter = painterResource(R.drawable.ic_hamburger),
                            tint = colorResource(R.color.white),
                            contentDescription = "Open Drawer"
                        )
                    }
                }
                else ->{
                    IconButton(
                        onClick = {
                            pop()
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            painter = painterResource(R.drawable.arrow_left),
                            tint = colorResource(R.color.white),
                            contentDescription = "Go back"
                        )
                    }
                }
            }
        })
}