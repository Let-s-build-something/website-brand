package augmy.interactive.com.ui.landing

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import augmy.interactive.com.base.LocalDeviceType
import augmy.interactive.com.base.theme.Colors
import augmy.interactive.com.data.NetworkItemIO
import augmy.interactive.com.theme.LocalTheme
import augmy.interactive.com.theme.SharedColors
import augmy.interactive.com.ui.components.AvatarImage
import augmy.interactive.com.ui.components.ComponentHeaderButton
import augmy.interactive.com.ui.components.NetworkItemRow
import augmy.interactive.com.ui.landing.demo.GardenContent
import augmy.interactive.com.ui.landing.demo.MiniatureIndicator
import augmy.interactive.com.ui.landing.demo.RandomFlowerField
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import website_brand.composeapp.generated.resources.Res
import website_brand.composeapp.generated.resources.app_calendar
import website_brand.composeapp.generated.resources.app_calendar_cs
import website_brand.composeapp.generated.resources.app_graph
import website_brand.composeapp.generated.resources.app_graph_cs
import website_brand.composeapp.generated.resources.app_home
import website_brand.composeapp.generated.resources.app_home_cs
import website_brand.composeapp.generated.resources.head
import website_brand.composeapp.generated.resources.landing_demo_disclaimer
import website_brand.composeapp.generated.resources.landing_demo_others_action_closeness
import website_brand.composeapp.generated.resources.landing_demo_others_action_interact
import website_brand.composeapp.generated.resources.landing_demo_others_heading
import website_brand.composeapp.generated.resources.landing_demo_others_message
import website_brand.composeapp.generated.resources.landing_demo_you_heading
import website_brand.composeapp.generated.resources.landing_garden_description
import website_brand.composeapp.generated.resources.landing_problem_heading
import website_brand.composeapp.generated.resources.landing_problem_quote_0
import website_brand.composeapp.generated.resources.landing_problem_quote_1
import website_brand.composeapp.generated.resources.landing_problem_quote_2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LargeLayout(
    scrollState: ScrollState,
    horizontalPadding: Dp,
    verticalPadding: Dp
) {
    val language = Locale.current.language

    Column(Modifier.padding(horizontal = 12.dp)) {
        Spacer(Modifier.height(verticalPadding * 1.5f))
        Row {
            SelectionContainer(
                modifier = Modifier.weight(.7f)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.landing_problem_heading),
                        style = LocalTheme.current.styles.heading.copy(textAlign = TextAlign.Center)
                    )

                    Quote(
                        modifier = Modifier.padding(top = 8.dp),
                        text = stringResource(Res.string.landing_problem_quote_2)
                    )
                    Quote(
                        text = stringResource(Res.string.landing_problem_quote_0)
                    )
                    Quote(
                        text = stringResource(Res.string.landing_problem_quote_1)
                    )
                }
            }

            Spacer(Modifier.weight(.1f))

            Column(
                modifier = Modifier
                    .weight(.8f, fill = false)
                    .padding(top = 6.dp)
            ) {
                SelectionContainer {
                    Text(
                        text = stringResource(Res.string.landing_garden_description),
                        style = LocalTheme.current.styles.subheading.copy(textAlign = TextAlign.Center)
                    )
                }

                AnimatedGarden()
            }
        }

        Spacer(Modifier.height(verticalPadding * 2))

        SelectionContainer(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(
                text = stringResource(Res.string.landing_demo_you_heading),
                style = LocalTheme.current.styles.heading
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .background(
                    color = Colors.ProximityIntimate.copy(alpha = .4f),
                    shape = LocalTheme.current.shapes.rectangularActionShape
                ),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            GardenContent(
                modifier = Modifier
                    .weight(1f)
                    .padding(LocalTheme.current.shapes.componentCornerRadius)
                    .background(
                        color = LocalTheme.current.colors.backgroundLight,
                        shape = LocalTheme.current.shapes.componentShape
                    )
            )
            Image(
                modifier = Modifier
                    .weight(1f)
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
            Image(
                modifier = Modifier
                    .weight(1f)
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

        Spacer(Modifier.height(verticalPadding))

        SelectionContainer(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = verticalPadding)
        ) {
            Text(
                text = stringResource(Res.string.landing_demo_others_heading),
                style = LocalTheme.current.styles.heading.copy(textAlign = TextAlign.Center)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .background(
                    color = Colors.ProximityContacts.copy(alpha = .4f),
                    shape = LocalTheme.current.shapes.rectangularActionShape
                )
        ) {
            DemoOthersUser(modifier = Modifier.weight(1f))
            Spacer(Modifier.weight(.3f))
            Row(
                modifier = Modifier.weight(.7f),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    modifier = Modifier
                        .weight(1f)
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

        Spacer(Modifier.height(verticalPadding * 2))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoOthersUser(modifier: Modifier = Modifier) {
    val isCompact = LocalDeviceType.current == WindowWidthSizeClass.Compact

    Column(modifier = modifier) {
        val item = NetworkItemIO(
            displayName = "jacob",
            userId = "@jacob:augmy.org",
            lastMessage = stringResource(Res.string.landing_demo_others_message)
        )

        NetworkItemRow(
            modifier = Modifier
                .padding(LocalTheme.current.shapes.componentCornerRadius)
                .background(
                    color = LocalTheme.current.colors.backgroundLight,
                    shape = LocalTheme.current.shapes.componentShape
                )
                .padding(start = 6.dp, end = 10.dp),
            avatarSize = if (isCompact) 28.dp else 48.dp,
            data = item,
            content = {
                Row(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val unreadCount = 2
                    AnimatedVisibility(
                        visible = unreadCount > 0,
                        exit = scaleOut(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioNoBouncy,
                                stiffness = Spring.StiffnessMedium
                            ),
                            transformOrigin = TransformOrigin.Center
                        ) + fadeOut(),
                        enter = scaleIn(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessMedium
                            ),
                            transformOrigin = TransformOrigin.Center
                        ) + fadeIn(),
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .background(
                                    color = LocalTheme.current.colors.brandMain,
                                    shape = LocalTheme.current.shapes.rectangularActionShape
                                )
                                .minWidthEqualsHeight()
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(vertical = 4.dp, horizontal = 6.dp)
                                    .align(Alignment.Center),
                                text = when {
                                    unreadCount > 99 -> "99+"
                                    unreadCount > 0 -> unreadCount.toString()
                                    else -> ""
                                },
                                style = LocalTheme.current.styles.category.copy(
                                    color = LocalTheme.current.colors.brandMainDark
                                )
                            )
                        }
                    }

                    MiniatureIndicator(
                        valence = Emotion.Fulfillment.valence,
                        arousal = Emotion.Fulfillment.arousal,
                        seed = 101L
                    )
                }
            }
        )

        Column(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(if (isCompact) 1f else .85f)
                .padding(top = 24.dp)
                .background(
                    color = LocalTheme.current.colors.backgroundLight,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
                .padding(
                    start = if (isCompact) 4.dp else 8.dp,
                    end = if (isCompact) 8.dp else 16.dp,
                    bottom = if (isCompact) 12.dp else 24.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BottomSheetDefaults.DragHandle(color = LocalTheme.current.colors.secondary)

            Column(
                modifier = Modifier.padding(start = 8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box {
                        AvatarImage(
                            modifier = Modifier.sizeIn(
                                maxWidth = 125.dp,
                                maxHeight = 125.dp
                            ),
                            media = item.avatar,
                            name = item.displayName,
                            tag = item.tag,
                            animate = true
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(24.dp)
                                .background(
                                    color = LocalTheme.current.colors.backgroundLight,
                                    shape = CircleShape
                                )
                                .padding(2.dp)
                                .background(
                                    color = SharedColors.GREEN_CORRECT,
                                    shape = CircleShape
                                )
                        )
                    }
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = buildAnnotatedString {
                            item.displayName?.let {
                                withStyle(SpanStyle(fontSize = LocalTheme.current.styles.subheading.fontSize)) {
                                    append(it)
                                }
                            }
                            item.userId?.let { userId ->
                                withStyle(SpanStyle(color = LocalTheme.current.colors.disabled)) {
                                    append(" (${userId})")
                                }
                            }
                        },
                        style = LocalTheme.current.styles.regular.copy(
                            color = LocalTheme.current.colors.secondary
                        )
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.End)
                ) {
                    ComponentHeaderButton(
                        modifier = Modifier.animateContentSize(),
                        endImageVector = Icons.Outlined.TrackChanges,
                        text = stringResource(Res.string.landing_demo_others_action_closeness),
                        onClick = {}
                    )
                    ComponentHeaderButton(
                        endImageVector = Icons.AutoMirrored.Outlined.Chat,
                        text = stringResource(Res.string.landing_demo_others_action_interact),
                        onClick = {}
                    )
                }
            }

            Column(
                Modifier
                    .padding(top = 32.dp)
                    .requiredWidthIn(max = 250.dp)
            ) {
                val density = LocalDensity.current
                val potHeightDp = remember { mutableStateOf(0f) }

                Box(
                    Modifier
                        .padding(end = 4.dp)
                        .fillMaxWidth(.917f)
                        .align(Alignment.End),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    RandomFlowerField(
                        baseString = "garden",
                        potHeightDp = potHeightDp,
                        growth = 1f,
                        arousal = 0.1f
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
        }
    }
}

fun Modifier.minWidthEqualsHeight(): Modifier = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    val width = maxOf(placeable.width, placeable.height)
    val height = placeable.height
    layout(width, height) {
        placeable.placeRelative((width - placeable.width) / 2, 0)
    }
}
