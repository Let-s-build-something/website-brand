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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import augmy.interactive.com.base.LocalContentSizeDp
import augmy.interactive.com.base.LocalDeviceType
import augmy.interactive.com.base.MaxModalWidthDp
import augmy.interactive.com.base.ModalScreenContent
import augmy.interactive.com.base.theme.Colors
import augmy.interactive.com.base.theme.draggable
import augmy.interactive.com.shared.SharedViewModel
import augmy.interactive.com.shared.Utils.onEnter
import augmy.interactive.com.theme.LocalTheme
import augmy.interactive.com.ui.components.BrandHeaderButton
import augmy.interactive.com.ui.components.CustomTextField
import augmy.interactive.com.ui.components.SimpleModalBottomSheet
import augmy.interactive.com.ui.components.buildAnnotatedLink
import augmy.interactive.com.ui.landing.components.DemoOthersUser
import augmy.interactive.com.ui.landing.components.avatar.AvatarConfiguration
import augmy.interactive.com.ui.landing.demo.GardenContent
import kotlinx.browser.window
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import website_brand.composeapp.generated.resources.Res
import website_brand.composeapp.generated.resources.app_calendar
import website_brand.composeapp.generated.resources.app_calendar_cs
import website_brand.composeapp.generated.resources.app_feed
import website_brand.composeapp.generated.resources.app_graph
import website_brand.composeapp.generated.resources.app_graph_cs
import website_brand.composeapp.generated.resources.app_home
import website_brand.composeapp.generated.resources.app_home_cs
import website_brand.composeapp.generated.resources.landing_demo_disclaimer
import website_brand.composeapp.generated.resources.landing_demo_others_content
import website_brand.composeapp.generated.resources.landing_demo_others_cta
import website_brand.composeapp.generated.resources.landing_demo_others_heading
import website_brand.composeapp.generated.resources.landing_demo_you_content
import website_brand.composeapp.generated.resources.landing_demo_you_cta
import website_brand.composeapp.generated.resources.landing_demo_you_heading
import website_brand.composeapp.generated.resources.landing_heading
import website_brand.composeapp.generated.resources.landing_heading_prefix
import website_brand.composeapp.generated.resources.landing_heading_suffix
import website_brand.composeapp.generated.resources.landing_illustrator_credit
import website_brand.composeapp.generated.resources.landing_illustrator_credit_link_text
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
        add(if (days < 10) "0${days}d" else "${days}d")
        add(if (hours < 10) "0${hours}h" else "${hours}h")
        add(if (minutes < 10) "0${minutes}m" else "${minutes}m")
        add(if (seconds < 10) "0${seconds}s" else "${seconds}s")
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingScreen(model: SharedViewModel) {
    val contentSize = LocalContentSizeDp.current
    val verticalPadding = (contentSize.height / 16).dp
    val language = Locale.current.language
    val freeSpots = model.betaSpots.collectAsState()
    val signedUp = rememberSaveable { mutableStateOf(false) }
    val showSignUp = rememberSaveable { mutableStateOf(false) }
    val launchDate = remember {
        mutableStateOf(
            LocalDateTime(
                year = 2026,
                month = 1,
                day = 16,
                hour = 16,
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

    if (showSignUp.value) {
        SimpleModalBottomSheet(
            onDismissRequest = {
                showSignUp.value = false
            },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SelectionContainer {
                Text(
                    modifier = Modifier.animateContentSize(),
                    text = stringResource(
                        Res.string.landing_sign_up_info,
                        durationToLaunch.value?.toPrettyComponents() ?: ""
                    ),
                    maxLines = 1,
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

            Spacer(Modifier.height(16.dp))

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
                                backgroundColor = LocalTheme.current.colors.backgroundDark,
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

            Spacer(Modifier.height(42.dp))
        }
    }

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
                    text = stringResource(Res.string.landing_heading_prefix),
                    style = LocalTheme.current.styles.regular.copy(textAlign = TextAlign.Center)
                )
                Text(
                    text = stringResource(Res.string.landing_heading),
                    style = LocalTheme.current.styles.heading.copy(textAlign = TextAlign.Center)
                )
                Text(
                    modifier = Modifier.padding(top = 2.dp),
                    text = stringResource(Res.string.landing_heading_suffix),
                    style = LocalTheme.current.styles.regular.copy(textAlign = TextAlign.Center)
                )
            }
        }

        BrandHeaderButton(
            text = stringResource(Res.string.landing_sign_up),
            endImageVector = if (showSignUp.value) Icons.AutoMirrored.Outlined.Send else null,
            onClick = {
                showSignUp.value = true
            }
        )

        Column {
            Crossfade(LocalDeviceType.current == WindowWidthSizeClass.Compact) { isCompact ->
                if(isCompact) {
                    CompactLayout(verticalPadding = verticalPadding)
                }else {
                    LargeLayout(
                        verticalPadding = verticalPadding,
                        showSignUp = showSignUp
                    )
                }
            }

            SelectionContainer(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(Res.string.landing_demo_you_content),
                        style = LocalTheme.current.styles.regular
                    )
                    Text(
                        text = stringResource(Res.string.landing_demo_you_heading),
                        style = LocalTheme.current.styles.heading
                    )
                }
            }

            val demoYouScrollState = rememberScrollState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .horizontalScroll(demoYouScrollState)
                    .draggable(state = demoYouScrollState, orientation = Orientation.Horizontal)
                    .background(
                        color = Colors.ProximityIntimate.copy(alpha = .4f),
                        shape = LocalTheme.current.shapes.rectangularActionShape
                    ),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val screenWidth = contentSize.width
                    .coerceAtMost(MaxModalWidthDp.toFloat())
                    .times(.33f)
                    .coerceAtLeast(400f)
                    .minus(12f)
                    .dp

                GardenContent(
                    modifier = Modifier
                        .width(screenWidth)
                        .padding(LocalTheme.current.shapes.componentCornerRadius)
                        .background(
                            color = LocalTheme.current.colors.backgroundLight,
                            shape = LocalTheme.current.shapes.componentShape
                        ),
                    configuration = AvatarConfiguration(),
                    seed = ""
                )
                Image(
                    modifier = Modifier
                        .width(screenWidth)
                        .padding(LocalTheme.current.shapes.componentCornerRadius)
                        .background(
                            color = LocalTheme.current.colors.backgroundLight,
                            shape = LocalTheme.current.shapes.componentShape
                        )
                        .padding(LocalTheme.current.shapes.componentCornerRadius),
                    painter = painterResource(
                        if (language == "cs") Res.drawable.app_calendar_cs else Res.drawable.app_calendar
                    ),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth
                )
                Image(
                    modifier = Modifier
                        .width(screenWidth)
                        .padding(LocalTheme.current.shapes.componentCornerRadius)
                        .background(
                            color = LocalTheme.current.colors.backgroundLight,
                            shape = LocalTheme.current.shapes.componentShape
                        )
                        .padding(LocalTheme.current.shapes.componentCornerRadius),
                    painter = painterResource(
                        if (language == "cs") Res.drawable.app_graph_cs else Res.drawable.app_graph
                    ),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth
                )
            }
            SelectionContainer(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .align(Alignment.End)
            ) {
                Text(
                    text = stringResource(Res.string.landing_demo_disclaimer),
                    style = LocalTheme.current.styles.regular.copy(
                        fontSize = 14.sp,
                        color = LocalTheme.current.colors.disabled
                    )
                )
            }
            BrandHeaderButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(Res.string.landing_demo_you_cta)
            ) {
                showSignUp.value = true
            }

            Spacer(Modifier.height(verticalPadding))

            SelectionContainer(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = verticalPadding)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(Res.string.landing_demo_others_content),
                        style = LocalTheme.current.styles.regular
                    )
                    Text(
                        text = stringResource(Res.string.landing_demo_others_heading),
                        style = LocalTheme.current.styles.heading
                    )
                }
            }

            val demoOthersScrollState = rememberScrollState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(demoOthersScrollState)
                    .draggable(state = demoOthersScrollState, orientation = Orientation.Horizontal)
                    .padding(top = 10.dp)
                    .background(
                        color = Colors.ProximityContacts.copy(alpha = .4f),
                        shape = LocalTheme.current.shapes.rectangularActionShape
                    ),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val screenWidth = contentSize.width
                    .coerceAtMost(MaxModalWidthDp.toFloat())
                    .times(.33f)
                    .coerceAtLeast(400f)
                    .minus(12f)
                    .dp

                DemoOthersUser(
                    modifier = Modifier
                        .align(Alignment.Bottom)
                        .width(screenWidth)
                )
                Box {
                    Image(
                        modifier = Modifier
                            .width(screenWidth)
                            .padding(LocalTheme.current.shapes.componentCornerRadius)
                            .background(
                                color = LocalTheme.current.colors.backgroundLight,
                                shape = LocalTheme.current.shapes.componentShape
                            )
                            .padding(LocalTheme.current.shapes.componentCornerRadius),
                        painter = painterResource(
                            if (language == "cs") Res.drawable.app_home_cs else Res.drawable.app_home
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth
                    )
                    Box(
                        modifier = Modifier
                            .padding(LocalTheme.current.shapes.componentCornerRadius)
                            .background(
                                color = Color.Black.copy(alpha = .3f),
                                shape = LocalTheme.current.shapes.componentShape
                            )
                            .matchParentSize()
                    )
                }
                Box {
                    Image(
                        modifier = Modifier
                            .width(screenWidth)
                            .padding(LocalTheme.current.shapes.componentCornerRadius)
                            .background(
                                color = LocalTheme.current.colors.backgroundLight,
                                shape = LocalTheme.current.shapes.componentShape
                            )
                            .padding(LocalTheme.current.shapes.componentCornerRadius),
                        painter = painterResource(Res.drawable.app_feed),
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth
                    )
                    Box(
                        modifier = Modifier
                            .padding(LocalTheme.current.shapes.componentCornerRadius)
                            .background(
                                color = Color.Black.copy(alpha = .4f),
                                shape = LocalTheme.current.shapes.componentShape
                            )
                            .matchParentSize()
                    )
                }
            }
            SelectionContainer(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .align(Alignment.End)
            ) {
                Text(
                    text = stringResource(Res.string.landing_demo_disclaimer),
                    style = LocalTheme.current.styles.regular.copy(
                        fontSize = 14.sp,
                        color = LocalTheme.current.colors.disabled
                    )
                )
            }

            BrandHeaderButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(Res.string.landing_demo_others_cta)
            ) {
                showSignUp.value = true
            }

            Spacer(Modifier.height(verticalPadding * 2))

            FooterBlock(verticalPadding)
        }

        Spacer(Modifier.height(verticalPadding))
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = buildAnnotatedLink(
                text = stringResource(Res.string.landing_illustrator_credit),
                linkTexts = listOf(
                    stringResource(Res.string.landing_illustrator_credit_link_text),
                ),
                onLinkClicked = { _, index ->
                    window.open("https://www.instagram.com/_ilustraterka_/")
                }
            ),
            style = LocalTheme.current.styles.regular
        )
    }
}
