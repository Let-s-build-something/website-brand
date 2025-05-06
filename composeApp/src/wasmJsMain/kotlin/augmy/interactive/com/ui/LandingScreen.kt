package augmy.interactive.com.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import augmy.interactive.com.base.LocalContentSizeDp
import augmy.interactive.com.base.LocalDeviceType
import augmy.interactive.com.base.LocalNavController
import augmy.interactive.com.base.ModalScreenContent
import augmy.interactive.com.base.theme.scalingClickable
import augmy.interactive.com.data.Asset
import augmy.interactive.com.navigation.NavigationNode
import augmy.interactive.com.theme.LocalTheme
import augmy.interactive.com.ui.components.AsyncImageThumbnail
import augmy.interactive.com.ui.components.AsyncSvgImage
import augmy.interactive.com.ui.components.BrandActionButton
import augmy.interactive.com.ui.components.IndicatedAction
import augmy.interactive.com.ui.components.SocialMediaBottomSheet
import augmy.interactive.com.ui.components.buildAnnotatedLink
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.Url
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
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
import website_brand.composeapp.generated.resources.landing_block_3_content
import website_brand.composeapp.generated.resources.landing_block_3_heading
import website_brand.composeapp.generated.resources.landing_download_button
import website_brand.composeapp.generated.resources.landing_download_content
import website_brand.composeapp.generated.resources.landing_download_heading
import website_brand.composeapp.generated.resources.landing_download_not_distributed
import website_brand.composeapp.generated.resources.landing_footer_action
import website_brand.composeapp.generated.resources.landing_footer_content
import website_brand.composeapp.generated.resources.landing_footer_heading
import website_brand.composeapp.generated.resources.landing_header_content
import website_brand.composeapp.generated.resources.landing_header_content_link
import website_brand.composeapp.generated.resources.landing_header_heading
import website_brand.composeapp.generated.resources.landing_message_0
import website_brand.composeapp.generated.resources.landing_message_0_enhanced
import website_brand.composeapp.generated.resources.landing_message_1
import website_brand.composeapp.generated.resources.landing_message_1_enhanced
import website_brand.composeapp.generated.resources.landing_message_2
import website_brand.composeapp.generated.resources.landing_message_2_enhanced
import website_brand.composeapp.generated.resources.landing_message_3
import website_brand.composeapp.generated.resources.landing_message_3_enhanced
import website_brand.composeapp.generated.resources.landing_message_4
import website_brand.composeapp.generated.resources.landing_message_4_enhanced
import website_brand.composeapp.generated.resources.landing_message_5_enhanced
import website_brand.composeapp.generated.resources.landing_message_6_enhanced
import website_brand.composeapp.generated.resources.landing_messages_enhanced_heading
import website_brand.composeapp.generated.resources.landing_messages_enhanced_text
import website_brand.composeapp.generated.resources.landing_messages_heading
import website_brand.composeapp.generated.resources.landing_messages_text

data class SimulatedMessage(
    val content: String,
    val isCurrentUser: Boolean
)

/** home/landing screen which is initially shown on the application */
@Composable
fun LandingScreen() {
    val navController = LocalNavController.current
    val verticalPadding = (LocalContentSizeDp.current.height / 8).dp
    val horizontalPadding = (LocalContentSizeDp.current.width / 20).dp

    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val isEnhanced = rememberSaveable {
        mutableStateOf(false)
    }
    val popIndex = rememberSaveable {
        mutableStateOf(-1)
    }
    val messages = if(isEnhanced.value) {
        listOf(
            SimulatedMessage(content = stringResource(Res.string.landing_message_0_enhanced), isCurrentUser = true),
            SimulatedMessage(content = stringResource(Res.string.landing_message_1_enhanced), isCurrentUser = false),
            SimulatedMessage(content = stringResource(Res.string.landing_message_2_enhanced), isCurrentUser = true),
            SimulatedMessage(content = stringResource(Res.string.landing_message_3_enhanced), isCurrentUser = false),
            SimulatedMessage(content = stringResource(Res.string.landing_message_4_enhanced), isCurrentUser = true),
            SimulatedMessage(content = stringResource(Res.string.landing_message_5_enhanced), isCurrentUser = true),
            SimulatedMessage(content = stringResource(Res.string.landing_message_6_enhanced), isCurrentUser = false)
        )
    }else listOf(
        SimulatedMessage(content = stringResource(Res.string.landing_message_0), isCurrentUser = true),
        SimulatedMessage(content = stringResource(Res.string.landing_message_1), isCurrentUser = false),
        SimulatedMessage(content = stringResource(Res.string.landing_message_2), isCurrentUser = true),
        SimulatedMessage(content = stringResource(Res.string.landing_message_3), isCurrentUser = false),
        SimulatedMessage(content = stringResource(Res.string.landing_message_4), isCurrentUser = true)
    )

    LaunchedEffect(isEnhanced.value) {
        coroutineScope.launch {
            delay(1000)
            while(popIndex.value < messages.lastIndex) {
                delay(1500)
                popIndex.value += 1
            }
        }
    }


    ModalScreenContent(
        modifier = Modifier.padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        scrollState = scrollState
    ) {
        SelectionContainer {
            Column {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = verticalPadding)
                        .fillMaxWidth(.8f),
                    text = stringResource(Res.string.landing_header_heading),
                    style = LocalTheme.current.styles.heading.copy(textAlign = TextAlign.Center)
                )
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 2.dp)
                        .fillMaxWidth(.8f),
                    text = buildAnnotatedLink(
                        text = stringResource(Res.string.landing_header_content),
                        linkTextWithin = stringResource(Res.string.landing_header_content_link),
                        onLinkClicked = {
                            coroutineScope.launch {
                                scrollState.animateScrollBy(2500f)
                            }
                        }
                    ),
                    style = LocalTheme.current.styles.regular.copy(textAlign = TextAlign.Center)
                )

                Spacer(Modifier.height(verticalPadding * 2))
            }
        }

        Row {
            Crossfade(
                modifier = Modifier.weight(.5f),
                targetState = isEnhanced.value
            ) { enhanced ->
                Column {
                    Row(
                        modifier = Modifier.scalingClickable(scaleInto = .95f) {
                            popIndex.value = -1
                            isEnhanced.value = !isEnhanced.value
                        }
                    ) {
                        Text(
                            text = stringResource(
                                if(enhanced) Res.string.landing_messages_enhanced_heading
                                else Res.string.landing_messages_heading
                            ),
                            style = LocalTheme.current.styles.subheading
                        )
                        Switch(
                            modifier = Modifier.padding(start = 4.dp),
                            checked = enhanced,
                            onCheckedChange = { isEnhanced.value = !isEnhanced.value }
                        ) // TODO colors are weird for unselected
                    }
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = stringResource(
                            if(enhanced) Res.string.landing_messages_enhanced_text
                            else Res.string.landing_messages_text
                        ),
                        style = LocalTheme.current.styles.regular
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                messages.forEachIndexed { index, message ->
                    MessageRow(
                        message = message,
                        hasPrevious = index > 0,
                        hasNext = index < messages.lastIndex,
                        show = popIndex.value >= index
                    )
                }
            }
        }

        Spacer(Modifier.height(verticalPadding))

        SelectionContainer {
            Column {
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

                DownloadBlock()

                Spacer(Modifier.height(verticalPadding))

                Column(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(.8f)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(Res.string.landing_footer_heading),
                        style = LocalTheme.current.styles.heading.copy(textAlign = TextAlign.Center)
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(Res.string.landing_footer_content),
                        style = LocalTheme.current.styles.regular.copy(textAlign = TextAlign.Center)
                    )
                    IndicatedAction(
                        modifier = Modifier.align(Alignment.End),
                        content = { modifier ->
                            Text(
                                modifier = modifier,
                                text = stringResource(Res.string.landing_footer_action),
                                style = LocalTheme.current.styles.regular
                            )
                        },
                        onPress = {
                            navController?.navigate(NavigationNode.PublicAbout.route)
                        }
                    )
                }

                Spacer(Modifier.height(verticalPadding))
            }
        }
    }
}

@Composable
private fun ColumnScope.MessageRow(
    message: SimulatedMessage,
    hasPrevious: Boolean,
    hasNext: Boolean,
    show: Boolean
) {
    AnimatedVisibility(
        visible = show,
        enter = fadeIn() + slideInHorizontally { if(message.isCurrentUser) it else -it } + expandVertically()
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = if(message.isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
        ) {
            MessageBubble(
                modifier = Modifier
                    .fillMaxWidth(.6f)
                    .padding(bottom = 8.dp),
                text = message.content,
                isCurrentUser = message.isCurrentUser,
                hasPrevious = hasPrevious,
                hasNext = hasNext
            )
        }
    }
}

@Composable
private fun MessageBubble(
    modifier: Modifier = Modifier,
    hasPrevious: Boolean,
    isCurrentUser: Boolean,
    hasNext: Boolean,
    text: String,
) {
    val density = LocalDensity.current

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if(isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        if(!isCurrentUser) {
            AsyncSvgImage(
                modifier = Modifier
                    .size(
                        with(density) {
                            LocalTheme.current.styles.subheading.fontSize.toDp() + 20.dp
                        }
                    )
                    .clip(CircleShape),
                asset = Asset.Image.IAmJustAFight
            )
        }
        Spacer(Modifier.width(6.dp))
        Text(
            modifier = Modifier
                .background(
                    color = if(isCurrentUser) {
                        LocalTheme.current.colors.brandMainDark
                    }else LocalTheme.current.colors.brandMain,
                    shape = if (isCurrentUser) {
                        RoundedCornerShape(
                            topStart = 24.dp,
                            topEnd = if(hasPrevious) 1.dp else 24.dp,
                            bottomStart = 24.dp,
                            bottomEnd = if (hasNext) 1.dp else 24.dp
                        )
                    } else {
                        RoundedCornerShape(
                            topEnd = 24.dp,
                            topStart = if(hasPrevious) 1.dp else 24.dp,
                            bottomEnd = 24.dp,
                            bottomStart = if (hasNext) 1.dp else 24.dp
                        )
                    }
                )
                .padding(
                    vertical = 10.dp,
                    horizontal = 14.dp
                ),
            text = text,
            style = LocalTheme.current.styles.subheading.copy(color = Color.White)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ColumnScope.DownloadBlock() {
    val showSocialModal = rememberSaveable {
        mutableStateOf(false)
    }

    Text(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(Res.string.landing_download_heading),
        style = LocalTheme.current.styles.heading.copy(textAlign = TextAlign.Center)
    )
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(Res.string.landing_download_content),
        style = LocalTheme.current.styles.regular.copy(textAlign = TextAlign.Center)
    )

    if(showSocialModal.value) {
        SocialMediaBottomSheet(
            text = stringResource(Res.string.landing_download_not_distributed),
            onDismissRequest = {
                showSocialModal.value = false
            }
        )
    }

    BrandActionButton(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(top = 24.dp),
        onPress = {
            showSocialModal.value = true
        },
        text = stringResource(Res.string.landing_download_button),
        imageVector = Icons.Outlined.Download
    )
}

@Composable
private fun ColumnScope.FooterBlock(verticalPadding: Dp) {
    Text(
        modifier = Modifier
            .padding(top = verticalPadding)
            .fillMaxWidth(),
        text = stringResource(Res.string.landing_block_3_heading),
        style = LocalTheme.current.styles.heading.copy(textAlign = TextAlign.Center)
    )
    Text(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .widthIn(max = 700.dp)
            .fillMaxWidth(),
        text = stringResource(Res.string.landing_block_3_content),
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