package augmy.interactive.com.ui.landing

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.FormatQuote
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import augmy.interactive.com.base.LocalContentSizeDp
import augmy.interactive.com.base.LocalDeviceType
import augmy.interactive.com.base.ModalScreenContent
import augmy.interactive.com.base.theme.Colors
import augmy.interactive.com.base.theme.draggable
import augmy.interactive.com.shared.SharedViewModel
import augmy.interactive.com.theme.LocalTheme
import augmy.interactive.com.theme.SharedColors
import augmy.interactive.com.ui.components.BrandHeaderButton
import augmy.interactive.com.ui.components.CustomTextField
import augmy.interactive.com.ui.components.ToggleButton
import augmy.interactive.com.ui.landing.demo.Garden.FLOWER_POT_RATIO
import augmy.interactive.com.ui.landing.demo.RandomFlowerField
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import website_brand.composeapp.generated.resources.Res
import website_brand.composeapp.generated.resources.emotion_anger
import website_brand.composeapp.generated.resources.emotion_cheerfulness
import website_brand.composeapp.generated.resources.emotion_fear
import website_brand.composeapp.generated.resources.emotion_joy
import website_brand.composeapp.generated.resources.emotion_love
import website_brand.composeapp.generated.resources.emotion_prompt
import website_brand.composeapp.generated.resources.emotion_sadness
import website_brand.composeapp.generated.resources.head
import website_brand.composeapp.generated.resources.landing_heading
import website_brand.composeapp.generated.resources.landing_heading_description
import website_brand.composeapp.generated.resources.landing_sign_up
import website_brand.composeapp.generated.resources.landing_sign_up_disabled
import website_brand.composeapp.generated.resources.landing_sign_up_hint
import website_brand.composeapp.generated.resources.landing_sign_up_info
import website_brand.composeapp.generated.resources.landing_sign_up_info_slots
import website_brand.composeapp.generated.resources.landing_sign_up_send
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime


private val emailAddressRegex = """[a-zA-Z0-9+._%-+]{1,256}@[a-zA-Z0-9][a-zA-Z0-9-]{0,64}(\.[a-zA-Z0-9][a-zA-Z0-9-]{0,25})+""".toRegex()

fun Duration.toPrettyComponents(): String {
    var remaining = this

    val days = remaining.inWholeDays
    remaining -= days.days

    val hours = remaining.inWholeHours
    remaining -= hours.hours

    val minutes = remaining.inWholeMinutes
    remaining -= minutes.minutes

    val seconds = remaining.inWholeSeconds

    val parts = buildList {
        add(if (days < 10) "${days}0d" else "${days}d")
        add(if (hours < 10) "${hours}0h" else "${hours}h")
        add(if (minutes < 10) "${minutes}0m" else "${minutes}m")
        add(if (seconds < 10) "${seconds}0s" else "${seconds}s")
    }

    return if (parts.isEmpty()) "Launched!"
    else parts.joinToString(" ")
}

@OptIn(ExperimentalTime::class)
fun between(dateTime: LocalDateTime?, dateTime2: LocalDateTime?): Duration? {
    if (dateTime == null || dateTime2 == null) return null
    return dateTime2.toInstant(TimeZone.currentSystemDefault())
        .minus(dateTime.toInstant(TimeZone.currentSystemDefault()))
}

@Composable
fun LandingScreen(model: SharedViewModel) {
    val verticalPadding = (LocalContentSizeDp.current.height / 8).dp
    val freeSpots = model.betaSpots.collectAsState()
    val signedUp = rememberSaveable { mutableStateOf(false) }
    val showSignUp = rememberSaveable { mutableStateOf(false) }
    val launchDate = remember {
        mutableStateOf(
            LocalDateTime(
                year = 2026,
                month = 1,
                day = 16,
                hour = 12,
                minute = 0,
                second = 0
            )
        )
    }
    val durationToLaunch = remember {
        mutableStateOf(
            between(
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                launchDate.value
            )
        )
    }

    LaunchedEffect(Unit) {
        while ((durationToLaunch.value?.inWholeMilliseconds ?: 0) > 0) {
            delay(1_000)
            durationToLaunch.value = between(
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                launchDate.value
            )
        }
    }

    val scrollState = rememberScrollState()


    ModalScreenContent(
        horizontalAlignment = Alignment.CenterHorizontally,
        scrollState = scrollState
    ) {
        SelectionContainer(
            modifier = Modifier
                .padding(top = verticalPadding)
                .padding(horizontal = 12.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(Res.string.landing_heading),
                    style = LocalTheme.current.styles.heading.copy(textAlign = TextAlign.Center)
                )
                Text(
                    modifier = Modifier.padding(top = 2.dp),
                    text = stringResource(Res.string.landing_heading_description),
                    style = LocalTheme.current.styles.regular.copy(textAlign = TextAlign.Center)
                )
            }
        }

        Column(
            modifier = Modifier
                .then(
                    if (showSignUp.value) {
                        Modifier.background(
                            color = LocalTheme.current.colors.backgroundDark,
                            shape = LocalTheme.current.shapes.componentShape
                        )
                    }else Modifier
                )
                .padding(vertical = 16.dp, horizontal = 12.dp)
                .fillMaxWidth()
                .animateContentSize(alignment = Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showSignUp.value) {
                Spacer(Modifier.height(32.dp))
                SelectionContainer {
                    Text(
                        modifier = Modifier.animateContentSize(),
                        text = stringResource(
                            Res.string.landing_sign_up_info,
                            durationToLaunch.value?.toPrettyComponents() ?: ""
                        ),
                        style = LocalTheme.current.styles.subheading
                    )
                }
                SelectionContainer {
                    Text(
                        text = stringResource(
                            Res.string.landing_sign_up_info_slots,
                            freeSpots.value - if (signedUp.value) 1 else 0
                        ),
                        style = LocalTheme.current.styles.regular
                    )
                }
            }
            if (!signedUp.value) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val state = rememberSaveable(TextFieldState.Saver) {
                        TextFieldState()
                    }
                    val isFocused = remember {
                        mutableStateOf(false)
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f, fill = false)
                            .animateContentSize()
                    ) {
                        if (showSignUp.value) {
                            CustomTextField(
                                modifier = Modifier
                                    .widthIn(max = 450.dp)
                                    .fillMaxWidth()
                                    .padding(end = 8.dp)
                                    .onEnter {
                                        if (showSignUp.value && emailAddressRegex.matches(state.text.toString())) {
                                            signedUp.value = true
                                            model.signUp(state.text)
                                        }
                                    },
                                backgroundColor = LocalTheme.current.colors.backgroundLight,
                                errorText = if (emailAddressRegex.matches(state.text.toString()) || state.text.isEmpty() || isFocused.value) {
                                    null
                                } else " ",
                                isFocused = isFocused,
                                state = state,
                                hint = stringResource(Res.string.landing_sign_up_hint)
                            )
                        }
                    }
                    BrandHeaderButton(
                        modifier = Modifier.animateContentSize(),
                        text = stringResource(
                            when {
                                freeSpots.value <= 0 -> Res.string.landing_sign_up_disabled
                                showSignUp.value -> Res.string.landing_sign_up_send
                                else -> Res.string.landing_sign_up
                            }
                        ),
                        isEnabled = (!showSignUp.value || emailAddressRegex.matches(state.text.toString()))
                                && freeSpots.value > 0,
                        endImageVector = if (showSignUp.value) Icons.AutoMirrored.Outlined.Send else null,
                        onClick = {
                            showSignUp.value = true
                            if (showSignUp.value && emailAddressRegex.matches(state.text.toString())) {
                                signedUp.value = true
                                model.signUp(state.text)
                            }else isFocused.value = false
                        }
                    )
                }
            }
        }

        Column {
            Crossfade(LocalDeviceType.current == WindowWidthSizeClass.Compact) { isCompact ->
                if(isCompact) {
                    CompactLayout(verticalPadding = verticalPadding)
                }else {
                    LargeLayout(verticalPadding = verticalPadding)
                }
            }

            FooterBlock(verticalPadding)
        }

        Spacer(Modifier.height(verticalPadding))
    }
}

@Composable
fun Quote(
    modifier: Modifier = Modifier,
    showBackground: Boolean = true,
    author: String,
    text: String,
) {
    Column(modifier) {
        Row(
            modifier = if (showBackground) {
                Modifier
                    .background(
                        color = LocalTheme.current.colors.backgroundDark.copy(alpha = .7f),
                        shape = LocalTheme.current.shapes.componentShape
                    )
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            }else Modifier
        ) {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .rotate(180f),
                imageVector = Icons.Outlined.FormatQuote,
                tint = LocalTheme.current.colors.secondary,
                contentDescription = null
            )

            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 6.dp)
                    .align(Alignment.CenterVertically)
                    .weight(1f, fill = false),
                text = text,
                style = LocalTheme.current.styles.regular.copy(
                    textAlign = TextAlign.Center
                )
            )

            Icon(
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .size(24.dp),
                imageVector = Icons.Outlined.FormatQuote,
                tint = LocalTheme.current.colors.secondary,
                contentDescription = null
            )
        }
        Text(
            modifier = Modifier
                .padding(top = 2.dp, bottom = 6.dp)
                .align(Alignment.End),
            text = author,
            style = LocalTheme.current.styles.regular.copy(
                fontSize = 16.sp
            )
        )
    }
}

sealed class Emotion(
    val arousal: Float,
    val valence: Float,
    val color: Color,
    val res: StringResource
) {
    object Annoyance: Emotion(
        valence = -0.666f,
        arousal = 0.436f,
        color = SharedColors.RED_ERROR,
        res = Res.string.emotion_anger
    )
    object Panic: Emotion(
        valence = -0.876f,
        arousal = 0.976f,
        color = Colors.GrayLight,
        res = Res.string.emotion_fear
    )
    object Fulfillment: Emotion(
        valence = 0.666f,
        arousal = .05f,
        color = Colors.Flax,
        res = Res.string.emotion_joy
    )
    object Cheerfulness: Emotion(
        valence = 0.938f,
        arousal = 0.604f,
        color = Colors.Flax,
        res = Res.string.emotion_cheerfulness
    )
    object Love: Emotion(
        valence = 0.996f,
        arousal = 0.334f,
        color = Colors.ProximityIntimate,
        res = Res.string.emotion_love
    )
    object Grief: Emotion(
        valence = -0.860f,
        arousal = 0.280f,
        color = Colors.ProximityContacts,
        res = Res.string.emotion_sadness
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AnimatedGarden(
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val scrollState = rememberScrollState()
    val potHeightDp = remember { mutableStateOf(0f) }

    val selectedEmotion = remember {
        mutableStateOf<Emotion>(Emotion.Fulfillment)
    }
    val emotions = listOf(
        Emotion.Love,
        Emotion.Annoyance,
        Emotion.Fulfillment,
        Emotion.Panic,
        Emotion.Cheerfulness,
        Emotion.Grief,
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            Modifier
                .padding(top = 16.dp)
                .widthIn(max = 400.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Box(
                Modifier
                    .padding(end = 4.dp)
                    .fillMaxWidth(.917f)
                    .heightIn(min = (potHeightDp.value * FLOWER_POT_RATIO * 1.8).dp)
                    .align(Alignment.End),
                contentAlignment = Alignment.BottomCenter
            ) {
                RandomFlowerField(
                    baseString = "garden",
                    potHeightDp = potHeightDp,
                    growth = selectedEmotion.value.valence,
                    arousal = selectedEmotion.value.arousal
                )
            }
            Image(
                modifier = Modifier
                    .zIndex(10f)
                    .onSizeChanged { coordinates ->
                        potHeightDp.value = with(density) { coordinates.height.toDp().value }
                    },
                painter = painterResource(Res.drawable.head),
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = LocalTheme.current.colors.gardenHead),
                alpha = .6f,
                contentScale = ContentScale.FillWidth
            )
        }
        Row(
            modifier = Modifier
                .horizontalScroll(scrollState)
                .draggable(state = scrollState, orientation = Orientation.Horizontal),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Spacer(Modifier.width(12.dp))
            emotions.forEach { emotion ->
                ToggleButton(
                    isSelected = selectedEmotion.value == emotion,
                    backgroundColor = emotion.color,
                    text = AnnotatedString(stringResource(emotion.res)),
                    onClick = {
                        selectedEmotion.value = emotion
                    }
                )
            }
            Spacer(Modifier.width(12.dp))
        }
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = stringResource(Res.string.emotion_prompt),
            style = LocalTheme.current.styles.regular.copy(
                fontSize = 16.sp
            )
        )
    }
}

fun Modifier.onEnter(
    enabled: Boolean = true,
    onEnter: (shiftPressed: Boolean) -> Unit
): Modifier = if (!enabled) this else this.onPreviewKeyEvent { event ->
    if (event.type == KeyEventType.KeyDown && event.key == Key.Enter) {
        onEnter(event.isShiftPressed)
        true
    } else false
}
