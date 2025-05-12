package augmy.interactive.com.ui.landing

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import augmy.interactive.com.theme.LocalTheme
import augmy.interactive.com.ui.landing.SimulatedMessage.Companion.beerAttention
import augmy.interactive.com.ui.landing.SimulatedMessage.Companion.beerBackground
import augmy.interactive.com.ui.landing.SimulatedMessage.Companion.coolCoolCoolBackground
import augmy.interactive.com.ui.landing.SimulatedMessage.Companion.decisiveFastBackground
import augmy.interactive.com.ui.landing.SimulatedMessage.Companion.howAreYouAttention
import augmy.interactive.com.ui.landing.SimulatedMessage.Companion.imOkayAttention
import augmy.interactive.com.ui.landing.SimulatedMessage.Companion.imOkayBackground
import augmy.interactive.com.ui.landing.SimulatedMessage.Companion.imOkayTimings
import augmy.interactive.com.ui.landing.SimulatedMessage.Companion.imSorryAttention
import augmy.interactive.com.ui.landing.SimulatedMessage.Companion.imSorryBackground
import augmy.interactive.com.ui.landing.SimulatedMessage.Companion.indecisiveBackground
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import website_brand.composeapp.generated.resources.Res
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
import website_brand.composeapp.generated.resources.landing_messages_heading
import kotlin.math.absoluteValue

private const val MAX_WIDTH_CHAT_DP = 360

@Composable
fun MessagesSection(
    scrollState: ScrollState,
    horizontalContent: @Composable (isEnhanced: Boolean) -> Unit = {},
    verticalContent: @Composable (isEnhanced: Boolean) -> Unit = {}
) {
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
            attention = imOkayAttention,
            background = imOkayBackground,
            timings = imOkayTimings
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
            horizontalContent(isEnhanced.value)

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
        
        verticalContent(isEnhanced.value)
    }
}