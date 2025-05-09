package augmy.interactive.com.ui.landing

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import augmy.interactive.com.data.Asset
import augmy.interactive.com.theme.LocalTheme
import augmy.interactive.com.ui.components.AsyncImageThumbnail
import augmy.interactive.com.ui.landing.social_circle.SocialCircle
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.Url
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import website_brand.composeapp.generated.resources.Res
import website_brand.composeapp.generated.resources.landing_block_0_content
import website_brand.composeapp.generated.resources.landing_block_0_heading
import website_brand.composeapp.generated.resources.landing_block_1_content
import website_brand.composeapp.generated.resources.landing_block_1_heading
import website_brand.composeapp.generated.resources.landing_block_2_content
import website_brand.composeapp.generated.resources.landing_block_2_heading
import website_brand.composeapp.generated.resources.landing_block_demo_heading
import website_brand.composeapp.generated.resources.landing_messages_enhanced_text
import website_brand.composeapp.generated.resources.landing_messages_text
import website_brand.composeapp.generated.resources.landing_social_circle_text
import kotlin.math.absoluteValue

@Composable
internal fun LargeLayout(
    scrollState: ScrollState,
    horizontalPadding: Dp,
    verticalPadding: Dp
) {
    Column(verticalArrangement = Arrangement.spacedBy(verticalPadding)) {
        SelectionContainer {
            Row(horizontalArrangement = Arrangement.spacedBy(horizontalPadding)) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.landing_block_0_heading),
                        style = LocalTheme.current.styles.heading
                    )
                    Text(
                        text = stringResource(Res.string.landing_block_0_content),
                        style = LocalTheme.current.styles.regular
                    )
                }
                Box(
                    Modifier
                        .weight(1f)
                        .padding(verticalPadding / 7)
                ) {
                    AsyncImageThumbnail(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(LocalTheme.current.shapes.componentShape),
                        asset = Asset.Image.NaturePalette
                    )
                }
            }
        }

        SelectionContainer {
            Row(horizontalArrangement = Arrangement.spacedBy(horizontalPadding)) {
                val composition by rememberLottieComposition {
                    LottieCompositionSpec.Url("https://lottie.host/49fb6653-3dbc-4925-b221-520ee02bd7af/j5WC6v0PFm.lottie")
                }

                Image(
                    modifier = Modifier
                        .sizeIn(maxHeight = 400.dp, maxWidth = 400.dp)
                        .aspectRatio(1f, matchHeightConstraintsFirst = true)
                        .background(
                            LocalTheme.current.colors.brandMainDark,
                            LocalTheme.current.shapes.componentShape
                        ),
                    painter = rememberLottiePainter(
                        composition = composition,
                        iterations = Int.MAX_VALUE,
                        speed = .75f
                    ),
                    contentDescription = null
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.landing_block_2_heading),
                        style = LocalTheme.current.styles.heading
                    )
                    Text(
                        text = stringResource(Res.string.landing_block_2_content),
                        style = LocalTheme.current.styles.regular
                    )
                }
            }
        }

        SelectionContainer {
            Row(horizontalArrangement = Arrangement.spacedBy(horizontalPadding)) {
                val composition by rememberLottieComposition {
                    LottieCompositionSpec.Url("https://lottie.host/6dc94a1e-3c98-4f74-a4b3-387c187fc377/iHZAJetUK8.lottie")
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.landing_block_1_heading),
                        style = LocalTheme.current.styles.heading
                    )
                    Text(
                        text = stringResource(Res.string.landing_block_1_content),
                        style = LocalTheme.current.styles.regular
                    )
                }

                Image(
                    modifier = Modifier
                        .sizeIn(maxHeight = 300.dp, maxWidth = 300.dp)
                        .aspectRatio(1f, matchHeightConstraintsFirst = true)
                        .background(
                            LocalTheme.current.colors.brandMain,
                            LocalTheme.current.shapes.componentShape
                        ),
                    painter = rememberLottiePainter(
                        composition = composition,
                        iterations = Int.MAX_VALUE,
                        speed = .75f
                    ),
                    contentDescription = null
                )
            }
        }

        Spacer(Modifier.height(verticalPadding))

        DemoList(scrollState)
    }
}

@Composable
fun CarouselNavigator(
    modifier: Modifier = Modifier,
    cycleMs: Long = 40_000,
    pagerState: PagerState
) {
    val scope = rememberCoroutineScope()
    val progress = remember {
        Animatable(0.0f)
    }

    LaunchedEffect(pagerState.currentPage) {
        progress.animateTo(0f, animationSpec = tween(0))
        scope.coroutineContext.cancelChildren()
        scope.launch {
            while (progress.value <= 1f) {
                delay(cycleMs / 100)
                progress.animateTo(
                    progress.value + 0.01f,
                    animationSpec = tween(cycleMs.toInt() / 100)
                )
            }
        }
        delay(cycleMs)
        pagerState.scrollToPage(
            pagerState.currentPage.plus(1).takeIf { it < pagerState.pageCount } ?: 0
        )
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.CenterHorizontally)
    ) {
        for(i in 0 until pagerState.pageCount) {
            val width = animateFloatAsState(
                targetValue = if(i == pagerState.currentPage) 48f else 16f
            )

            LinearProgressIndicator(
                modifier = Modifier
                    .requiredHeight(12.dp)
                    .requiredWidth(width.value.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(interactionSource = null, indication = null) {
                        scope.launch {
                            pagerState.scrollToPage(i)
                        }
                    },
                progress = { if(i == pagerState.currentPage) progress.value else 0f },
                color = LocalTheme.current.colors.brandMain,
                trackColor = LocalTheme.current.colors.secondary,
                gapSize = 0.dp,
                strokeCap = StrokeCap.Square
            )
        }
    }
}

@Composable
private fun DemoList(scrollState: ScrollState) {
    val state = rememberPagerState(
        initialPage = 0,
        pageCount = { 2 }
    )
    val isVisible = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.onGloballyPositioned {
            isVisible.value = it.positionInWindow().y.toInt() in -it.size.height.absoluteValue..it.size.height.absoluteValue
                    || it.positionInWindow().y.toInt() in 0..scrollState.viewportSize
        }
    ) {
        Text(
            text = stringResource(Res.string.landing_block_demo_heading),
            style = LocalTheme.current.styles.heading,
        )

        HorizontalPager(
            modifier = Modifier.animateContentSize(),
            state = state,
            userScrollEnabled = false,
            pageContent = { index ->
                when(index) {
                    0 -> {
                        Row {
                            SelectionContainer(modifier = Modifier.weight(.5f)) {
                                Text(
                                    modifier = Modifier
                                        .background(
                                            color = LocalTheme.current.colors.backgroundDark,
                                            shape = LocalTheme.current.shapes.componentShape
                                        )
                                        .padding(horizontal = 16.dp, vertical = 24.dp),
                                    text = stringResource(Res.string.landing_social_circle_text),
                                    style = LocalTheme.current.styles.category
                                )
                            }

                            Spacer(Modifier.weight(.05f))

                            SocialCircle(
                                isVisible = isVisible,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 16.dp)
                            )
                        }
                    }
                    1 -> {
                        MessagesSection(
                            isVisible = isVisible,
                            horizontalContent = { isEnhanced ->
                                Text(
                                    modifier = Modifier
                                        .weight(.5f)
                                        .animateContentSize()
                                        .background(
                                            color = LocalTheme.current.colors.backgroundDark,
                                            shape = LocalTheme.current.shapes.componentShape
                                        )
                                        .padding(horizontal = 16.dp, vertical = 24.dp),
                                    text = stringResource(
                                        if(isEnhanced) Res.string.landing_messages_enhanced_text
                                        else Res.string.landing_messages_text
                                    ),
                                    style = LocalTheme.current.styles.category
                                )

                                Spacer(Modifier.weight(.125f))
                            }
                        )
                    }
                }
            }
        )

        AnimatedVisibility(
            modifier = Modifier
                .padding(top = 8.dp)
                .align(Alignment.CenterHorizontally),
            visible = isVisible.value
        ) {
            CarouselNavigator(pagerState = state)
        }
    }
}
