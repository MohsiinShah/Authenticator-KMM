package com.mohsin.auth.android.screens.onboarding

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mohsin.auth.android.R
import com.mohsin.auth.android.components.ActionButton
import com.mohsin.auth.android.components.TextContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(navigateToNext: () -> Unit){

    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()

    val pages = listOf(
        PageData(
            R.raw.onboarding_cloud, "Heading One", stringResource(R.string.splash_headline)
        ), PageData(
            R.raw.onboarding_qr, "Heading Two", stringResource(R.string.splash_headline)
        ), PageData(
            R.raw.onboarding_qr, "Heading Three", stringResource(R.string.splash_headline)
        )
    )

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {

            HorizontalPager(
                state = pagerState, modifier = Modifier.wrapContentHeight()
            ) { index ->
                val page = pages[index]
                OnboardingPage(
                    pagerState = pagerState,
                    lottieRes = page.lottieRes,
                    title = page.title,
                    description = page.description
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Box(modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center) {
                Column {
                    ActionButton(
                        modifier = Modifier
                            .width(200.dp)
                            .clickable {
                                if(pagerState.currentPage == pages.lastIndex){
                                    navigateToNext()
                                }else {
                                    val nextPage = (pagerState.currentPage + 1).coerceAtMost(3)
                                    scope.launch(Dispatchers.Main) {
                                        pagerState.scrollToPage(nextPage)
                                    }
                                }
                            }, text =if (pagerState.currentPage != pages.lastIndex) stringResource(R.string.next) else stringResource(
                            R.string.done
                        )
                    )

                    TextContent(
                        modifier = Modifier
                            .width(200.dp)
                            .alpha(0.4f)
                            .padding(top = 30.dp)
                            .clickable{
                                navigateToNext()
                            },
                        value = if (pagerState.currentPage != pages.lastIndex) stringResource(R.string.skip) else "",
                        textColor = colorResource(R.color.black_80),
                        fontSize = 20.sp,
                        fontFamily = FontFamily(
                            Font(R.font.readex_pro_medium)
                        ),
                        textAlign = TextAlign.Center
                    )

                }
            }

            Spacer(modifier = Modifier.weight(1f))

        }
    }

}

@Composable
fun OnboardingPage(
    pagerState: PagerState,
    lottieRes: Int,
    title: String,
    description: String,
) {

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(lottieRes))
    val progress by animateLottieCompositionAsState(
        composition = composition, iterations = LottieConstants.IterateForever
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Lottie animation

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .background(color = colorResource(R.color.primary_blue))
        ) {

            Image(painter = painterResource(R.drawable.ic_pattern),
                contentDescription = null)

            Column(
                modifier = Modifier.matchParentSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                LottieAnimation(
                    composition = composition, progress = { progress },
                    modifier = Modifier.fillMaxSize(0.5f))

            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        WormDotIndicator(pagerState = pagerState)

        Spacer(modifier = Modifier.height(16.dp))

        TextContent(
            value = title,
            textColor = colorResource(R.color.black),
            fontSize = 22.sp,
            fontFamily = FontFamily(
                Font(R.font.readex_pro_medium)
            )
        )

        TextContent(
            modifier = Modifier
                .alpha(0.4f)
                .padding(start = 20.dp, end = 20.dp, top = 10.dp),
            value = description,
            textColor = colorResource(R.color.black),
            fontSize = 15.sp,
            fontFamily = FontFamily(
                Font(R.font.readex_pro_light)
            ),
            textAlign = TextAlign.Center
        )

    }
}

@Composable
fun WormDotIndicator(pagerState: PagerState) {
    Row(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pagerState.pageCount) { index ->
            val isSelected = pagerState.currentPage == index
            val width by animateDpAsState(
                targetValue = if (isSelected) 40.dp else 8.dp, label = "dot_width"
            )
            val color = if (isSelected) {
                colorResource(R.color.primary_blue)
            } else {
                colorResource(R.color.primary_blue_20)
            }

            Box(
                modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .clip(CircleShape)
                    .background(color)
                    .height(8.dp)
                    .width(width)
            )
        }
    }
}

data class PageData(
    val lottieRes: Int,
    val title: String,
    val description: String,
)