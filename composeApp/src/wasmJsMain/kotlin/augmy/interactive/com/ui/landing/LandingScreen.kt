package augmy.interactive.com.ui.landing

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import augmy.interactive.com.base.LocalContentSizeDp
import augmy.interactive.com.base.LocalDeviceType
import augmy.interactive.com.base.LocalNavController
import augmy.interactive.com.base.ModalScreenContent
import augmy.interactive.com.navigation.NavigationNode
import augmy.interactive.com.theme.LocalTheme
import augmy.interactive.com.ui.components.IndicatedAction
import augmy.interactive.com.ui.components.buildAnnotatedLink
import augmy.interactive.com.ui.landing.SimulatedMessage.Companion.beerAttention
import augmy.interactive.com.ui.landing.SimulatedMessage.Companion.beerBackground
import augmy.interactive.com.ui.landing.SimulatedMessage.Companion.coolCoolCoolBackground
import augmy.interactive.com.ui.landing.SimulatedMessage.Companion.decisiveFastBackground
import augmy.interactive.com.ui.landing.SimulatedMessage.Companion.howAreYouAttention
import augmy.interactive.com.ui.landing.SimulatedMessage.Companion.imOkayAttention
import augmy.interactive.com.ui.landing.SimulatedMessage.Companion.imOkayBackground
import augmy.interactive.com.ui.landing.SimulatedMessage.Companion.imSorryAttention
import augmy.interactive.com.ui.landing.SimulatedMessage.Companion.imSorryBackground
import augmy.interactive.com.ui.landing.SimulatedMessage.Companion.indecisiveBackground
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import website_brand.composeapp.generated.resources.Res
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
import kotlin.math.absoluteValue

private const val MAX_WIDTH_CHAT_DP = 360

/** home/landing screen which is initially shown on the application */
@Composable
fun LandingScreen() {
    val navController = LocalNavController.current
    val verticalPadding = (LocalContentSizeDp.current.height / 8).dp
    val horizontalPadding = (LocalContentSizeDp.current.width / 20).dp

    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()


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
                                scrollState.animateScrollBy(5000f)
                            }
                        }
                    ),
                    style = LocalTheme.current.styles.regular.copy(textAlign = TextAlign.Center)
                )

                Spacer(Modifier.height(verticalPadding * 2))
            }
        }

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

                Spacer(Modifier.height(verticalPadding * 2))
                MessagesSection(scrollState)

                Spacer(Modifier.height(verticalPadding * 2))
                FooterBlock(verticalPadding)
                Spacer(Modifier.height(verticalPadding))
            }
        }

        DownloadBlock()

        Spacer(Modifier.height(verticalPadding))

        SelectionContainer {
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
        }

        Spacer(Modifier.height(verticalPadding))
    }
}

@Composable
private fun MessagesSection(scrollState: ScrollState) {
    val popIndexScope = rememberCoroutineScope()
    val attentionScope = rememberCoroutineScope()

    val isEnhanced = rememberSaveable {
        mutableStateOf(true)
    }
    val popIndex = rememberSaveable {
        mutableStateOf(-1)
    }
    val isVisible = remember {
        mutableStateOf(false)
    }
    val enhancedMessages = listOf(
        SimulatedMessage(
            content = stringResource(Res.string.landing_message_0_enhanced),
            isCurrentUser = true,
            attention = howAreYouAttention
        ),
        SimulatedMessage(
            content = stringResource(Res.string.landing_message_1_enhanced),
            isCurrentUser = false,
            isIndecisive = true,
            attention = imOkayAttention,
            background = imOkayBackground
        ),
        SimulatedMessage(
            content = stringResource(Res.string.landing_message_2_enhanced),
            isCurrentUser = true,
            background = imSorryBackground,
            attention = imSorryAttention
        ),
        SimulatedMessage(
            content = stringResource(Res.string.landing_message_3_enhanced),
            isCurrentUser = false,
            isIndecisive = true,
            background = indecisiveBackground
        ),
        SimulatedMessage(
            content = stringResource(Res.string.landing_message_4_enhanced),
            isCurrentUser = true,
            background = coolCoolCoolBackground
        ),
        SimulatedMessage(
            content = stringResource(Res.string.landing_message_5_enhanced),
            isCurrentUser = true,
            background = beerBackground,
            attention = beerAttention
        ),
        SimulatedMessage(
            content = stringResource(Res.string.landing_message_6_enhanced),
            isCurrentUser = false,
            background = decisiveFastBackground
        )
    )
    val regularMessages = listOf(
        SimulatedMessage(content = stringResource(Res.string.landing_message_0), isCurrentUser = true),
        SimulatedMessage(content = stringResource(Res.string.landing_message_1), isCurrentUser = false),
        SimulatedMessage(content = stringResource(Res.string.landing_message_2), isCurrentUser = true),
        SimulatedMessage(content = stringResource(Res.string.landing_message_3), isCurrentUser = false),
        SimulatedMessage(content = stringResource(Res.string.landing_message_4), isCurrentUser = true)
    )

    val currentMessage = (if(isEnhanced.value) enhancedMessages else regularMessages).getOrNull(popIndex.value)
    val attentionFraction = remember(isEnhanced.value) {
        Animatable(0.15f)
    }
    val cornerRadius = attentionFraction.value
        .times(64f)
        .dp

    LaunchedEffect(isEnhanced.value, isVisible.value) {
        popIndexScope.coroutineContext.cancelChildren()
        popIndexScope.launch {
            val lastIndex = (enhancedMessages.lastIndex.takeIf { isEnhanced.value }
                ?: regularMessages.lastIndex)

            while(popIndex.value < lastIndex && isVisible.value) {
                popIndex.value += 1
                delay(if(isEnhanced.value) 3500 else 1500)
            }
            if(!isEnhanced.value && isVisible.value) {
                delay(2000)
                isEnhanced.value = true
            }
        }
    }

    LaunchedEffect(currentMessage, isVisible.value) {
        if (currentMessage?.attention != null) {
            attentionScope.coroutineContext.cancelChildren()
            attentionScope.launch {
                currentMessage.attention.forEach { attention ->
                    delay(500)
                    attentionFraction.animateTo(
                        attention.second,
                        animationSpec = tween(attention.first.toInt())
                    )
                    delay(attention.first)
                }
            }
        }
    }

    Column(
        modifier = Modifier.onGloballyPositioned {
            isVisible.value = it.positionInWindow().y.toInt() in -it.size.height.absoluteValue..it.size.height.absoluteValue
                    || it.positionInWindow().y.toInt() in 0..scrollState.viewportSize
        },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.clickable(indication = null, interactionSource = null) {
                popIndex.value = -1
                isEnhanced.value = !isEnhanced.value
            },
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                modifier = Modifier.animateContentSize(),
                text = stringResource(
                    if(isEnhanced.value) Res.string.landing_messages_enhanced_heading
                    else Res.string.landing_messages_heading
                ),
                style = LocalTheme.current.styles.subheading
            )
            Switch(
                modifier = Modifier.padding(start = 4.dp),
                checked = isEnhanced.value,
                onCheckedChange = {
                    popIndex.value = -1
                    isEnhanced.value = !isEnhanced.value
                },
                colors = SwitchDefaults.colors(
                    uncheckedThumbColor = LocalTheme.current.colors.secondary,
                    uncheckedTrackColor = LocalTheme.current.colors.backgroundDark,
                    uncheckedBorderColor = LocalTheme.current.colors.disabled,
                    checkedThumbColor = LocalTheme.current.colors.secondary,
                    checkedTrackColor = LocalTheme.current.colors.brandMain,
                    checkedBorderColor = LocalTheme.current.colors.backgroundDark
                )
            )
        }
        Row(modifier = Modifier.padding(top = 12.dp)) {
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
                    if(isEnhanced.value) Res.string.landing_messages_enhanced_text
                    else Res.string.landing_messages_text
                ),
                style = LocalTheme.current.styles.category
            )

            Spacer(Modifier.weight(.125f))

            Crossfade(
                modifier = Modifier
                    .then(
                        if(popIndex.value > -1) {
                            Modifier.background(
                                color = if(isEnhanced.value) {
                                    LocalTheme.current.colors.brandMainDark.copy(alpha = attentionFraction.value)
                                }else LocalTheme.current.colors.backgroundDark,
                                shape = LocalTheme.current.shapes.componentShape
                            )
                        }else Modifier
                    )
                    .weight(1f),
                targetState = isEnhanced.value
            ) { enhanced ->
                Column(
                    modifier = Modifier
                        .padding(cornerRadius)
                        .then(
                            if(popIndex.value > -1) {
                                Modifier.background(
                                    color = LocalTheme.current.colors.backgroundLight,
                                    shape = RoundedCornerShape(cornerRadius)
                                )
                            }else Modifier
                        )
                        .padding(horizontal = 4.dp)
                        .fillMaxWidth()
                        .widthIn(max = MAX_WIDTH_CHAT_DP.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    (if(enhanced) enhancedMessages else regularMessages).let { messages ->
                        messages.forEachIndexed { index, message ->
                            MessageRow(
                                message = message,
                                hasPrevious = messages.getOrNull(index - 1)?.isCurrentUser == message.isCurrentUser,
                                hasNext = messages.getOrNull(index + 1)?.isCurrentUser == message.isCurrentUser,
                                show = popIndex.value >= index
                            )
                        }
                    }
                }
            }
        }
    }
}
