package augmy.interactive.com.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import augmy.interactive.com.base.LocalContentSizeDp
import augmy.interactive.com.base.LocalDeviceType
import augmy.interactive.com.base.LocalSnackbarHost
import augmy.interactive.com.base.ModalScreenContent
import augmy.interactive.com.data.Asset
import augmy.interactive.com.data.PlatformDistribution
import augmy.interactive.com.theme.LocalTheme
import augmy.interactive.com.ui.components.AsyncImageThumbnail
import augmy.interactive.com.ui.components.IndicatedAction
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.Url
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import kotlinx.browser.window
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import website_brand.composeapp.generated.resources.Res
import website_brand.composeapp.generated.resources.landing_block_0_content
import website_brand.composeapp.generated.resources.landing_block_0_heading
import website_brand.composeapp.generated.resources.landing_block_1_content
import website_brand.composeapp.generated.resources.landing_block_1_heading
import website_brand.composeapp.generated.resources.landing_block_2_content
import website_brand.composeapp.generated.resources.landing_block_2_heading
import website_brand.composeapp.generated.resources.landing_download_button
import website_brand.composeapp.generated.resources.landing_download_not_distributed
import website_brand.composeapp.generated.resources.landing_footer_content
import website_brand.composeapp.generated.resources.landing_footer_heading
import website_brand.composeapp.generated.resources.landing_header_content
import website_brand.composeapp.generated.resources.landing_header_heading

/** home/landing screen which is initially shown on the application */
@Composable
fun LandingScreen() {
    val verticalPadding = (LocalContentSizeDp.current.height / 8).dp
    val horizontalPadding = (LocalContentSizeDp.current.width / 20).dp

    val scrollState = rememberScrollState()

    ModalScreenContent(
        Modifier.padding(horizontal = 12.dp),
        scrollState = scrollState
    ) {
        SelectionContainer {
            Column {
                Text(
                    modifier = Modifier
                        .padding(top = verticalPadding)
                        .fillMaxWidth(),
                    text = stringResource(Res.string.landing_header_heading),
                    style = LocalTheme.current.styles.heading.copy(textAlign = TextAlign.Center)
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.landing_header_content),
                    style = LocalTheme.current.styles.regular.copy(textAlign = TextAlign.Center)
                )

                Spacer(Modifier.height(verticalPadding * 2))

                Crossfade(LocalDeviceType.current == WindowWidthSizeClass.Compact) { isCompact ->
                    if(isCompact) {
                        CompactLayout(verticalPadding = verticalPadding)
                    }else {
                        LargeLayout(
                            verticalPadding = verticalPadding,
                            horizontalPadding = horizontalPadding
                        )
                    }
                }

                Spacer(Modifier.height(verticalPadding))

                FooterBlock(verticalPadding)

                Spacer(Modifier.height(verticalPadding))
            }
        }
        DownloadBlock(scrollState)

        Spacer(Modifier.height(verticalPadding))
    }
}

@Composable
private fun ColumnScope.DownloadBlock(scrollState: ScrollState) {
    val isExpanded = rememberSaveable {
        mutableStateOf(false)
    }
    val scrollCoroutine = rememberCoroutineScope()

    IndicatedAction(
        modifier = Modifier.align(Alignment.Start),
        content = { modifier ->
            Text(
                modifier = modifier,
                text = stringResource(Res.string.landing_download_button),
                style = LocalTheme.current.styles.heading
            )
        },
        onPress = {
            isExpanded.value = !isExpanded.value
            if(isExpanded.value) {
                scrollCoroutine.launch {
                    scrollState.animateScrollBy(500f)
                }
            }
        },
        isSelected = isExpanded.value
    )

    AnimatedVisibility(visible = isExpanded.value) {
        Column(
            modifier = Modifier.padding(top = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val hoveredIndex = remember {
                mutableStateOf(-2)
            }

            PlatformDistribution.entries.forEachIndexed { index, platform ->
                val dividerColor by animateColorAsState(
                    if(index in hoveredIndex.value..hoveredIndex.value + 1) {
                        LocalTheme.current.colors.brandMain
                    }else LocalTheme.current.colors.secondary,
                    label = "animDividerColor$index"
                )

                HorizontalDivider(
                    modifier = Modifier.padding(start = 12.dp),
                    color = dividerColor
                )
                PlatformDownload(
                    icon = platform.imageVector,
                    text = platform.label,
                    url = platform.url,
                    onHovered = { isHovering ->
                        if(isHovering) {
                            hoveredIndex.value = index
                        }else if(index == hoveredIndex.value) {
                            hoveredIndex.value = -2
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun PlatformDownload(
    icon: ImageVector,
    text: String,
    url: String,
    onHovered: (Boolean) -> Unit
) {
    val snackbarHost = LocalSnackbarHost.current
    val notDistributedMessage = stringResource(Res.string.landing_download_not_distributed)
    val coroutine = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(onHovered) {
        interactionSource.interactions.collectLatest {
            when(it) {
                is HoverInteraction.Enter -> onHovered(true)
                is HoverInteraction.Exit -> onHovered(false)
            }
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = icon,
                contentDescription = null,
                tint = LocalTheme.current.colors.primary
            )
            Text(
                text = text,
                style = LocalTheme.current.styles.regular
            )
        }
        Icon(
            modifier = Modifier
                .size(32.dp)
                .hoverable(interactionSource)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = ripple(bounded = true),
                    onClick = {
                        if(url.isBlank()) {
                            coroutine.launch {
                                snackbarHost?.showSnackbar(
                                    message = notDistributedMessage
                                )
                            }
                        }else window.open(url)
                    }
                )
                .clip(CircleShape),
            imageVector = Icons.Outlined.Download,
            contentDescription = null,
            tint = LocalTheme.current.colors.primary
        )
    }
}

@Composable
private fun ColumnScope.FooterBlock(verticalPadding: Dp) {
    Text(
        modifier = Modifier
            .padding(top = verticalPadding)
            .fillMaxWidth(),
        text = stringResource(Res.string.landing_footer_heading),
        style = LocalTheme.current.styles.heading.copy(textAlign = TextAlign.Center)
    )
    Text(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .widthIn(max = 700.dp)
            .fillMaxWidth(),
        text = stringResource(Res.string.landing_footer_content),
        style = LocalTheme.current.styles.regular.copy(textAlign = TextAlign.Center)
    )

    val composition by rememberLottieComposition {
        LottieCompositionSpec.Url("https://lottie.host/93c91fb8-636b-448f-8e08-401564eb07e9/Zs9LeGKFON.lottie")
    }

    Image(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .sizeIn(maxHeight = 300.dp, maxWidth = 300.dp)
            .fillMaxWidth(0.8f)
            .aspectRatio(1f, matchHeightConstraintsFirst = true),
        painter = rememberLottiePainter(
            composition = composition,
            iterations = Int.MAX_VALUE,
            speed = 0.25f
        ),
        contentDescription = null
    )
}

@Composable
private fun CompactLayout(verticalPadding: Dp) {
    Column(modifier = Modifier.padding(horizontal = 12.dp)) {
        // first block
        Box(
            Modifier
                .fillMaxWidth()
                .background(
                    LocalTheme.current.colors.tetrial,
                    LocalTheme.current.shapes.roundShape
                )
                .padding(verticalPadding / 7)
        ) {
            AsyncImageThumbnail(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(LocalTheme.current.shapes.componentShape),
                asset = Asset.Image.NaturePalette
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
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

        Spacer(Modifier.height(verticalPadding))

        // second block
        val composition by rememberLottieComposition {
            LottieCompositionSpec.Url("https://lottie.host/6dc94a1e-3c98-4f74-a4b3-387c187fc377/iHZAJetUK8.lottie")
        }

        Image(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .sizeIn(maxHeight = 300.dp, maxWidth = 300.dp)
                .fillMaxWidth()
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

        Column(
            modifier = Modifier
                .fillMaxWidth()
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

        Spacer(Modifier.height(verticalPadding))

        val composition2 by rememberLottieComposition {
            LottieCompositionSpec.Url("https://lottie.host/49fb6653-3dbc-4925-b221-520ee02bd7af/j5WC6v0PFm.lottie")
        }

        Image(
            modifier = Modifier
                .fillMaxWidth()
                .sizeIn(maxHeight = 400.dp, maxWidth = 400.dp)
                .aspectRatio(1f, matchHeightConstraintsFirst = true)
                .background(
                    LocalTheme.current.colors.brandMainDark,
                    LocalTheme.current.shapes.componentShape
                ),
            painter = rememberLottiePainter(
                composition = composition2,
                iterations = Int.MAX_VALUE,
                speed = .75f
            ),
            contentDescription = null
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
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

@Composable
private fun LargeLayout(
    horizontalPadding: Dp,
    verticalPadding: Dp
) {
    Column(verticalArrangement = Arrangement.spacedBy(verticalPadding)) {
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
                    .background(
                        LocalTheme.current.colors.tetrial,
                        LocalTheme.current.shapes.roundShape
                    )
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
        Row(horizontalArrangement = Arrangement.spacedBy(horizontalPadding)) {
            val composition by rememberLottieComposition {
                LottieCompositionSpec.Url("https://lottie.host/6dc94a1e-3c98-4f74-a4b3-387c187fc377/iHZAJetUK8.lottie")
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
        }

        Row(horizontalArrangement = Arrangement.spacedBy(horizontalPadding)) {
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
        }
    }
}