package augmy.interactive.com.ui.landing.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import augmy.interactive.com.base.LocalDeviceType
import augmy.interactive.com.data.MediaIO
import augmy.interactive.com.data.NetworkItemIO
import augmy.interactive.com.theme.LocalTheme
import augmy.interactive.com.theme.SharedColors
import augmy.interactive.com.ui.components.AvatarImage
import augmy.interactive.com.ui.components.ComponentHeaderButton
import augmy.interactive.com.ui.components.NetworkItemRow
import augmy.interactive.com.ui.landing.components.avatar.AvatarConfiguration
import augmy.interactive.com.ui.landing.demo.MiniatureIndicator
import org.jetbrains.compose.resources.stringResource
import website_brand.composeapp.generated.resources.Res
import website_brand.composeapp.generated.resources.landing_demo_others_action_closeness
import website_brand.composeapp.generated.resources.landing_demo_others_action_interact
import website_brand.composeapp.generated.resources.landing_demo_others_message

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoOthersUser(modifier: Modifier = Modifier) {
    val isCompact = LocalDeviceType.current == WindowWidthSizeClass.Compact

    Column(modifier = modifier) {
        val item = NetworkItemIO(
            displayName = "jacob",
            userId = "@jacob:augmy.org",
            avatar = MediaIO(
                url = "https://augmy.org/storage/company/kostka_jakub_rectangle.jpg",
                thumbnail = "https://augmy.org/storage/company/thumbnails/tn_kostka_jakub_rectangle.jpg"
            ),
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
                        valence = 1f,
                        arousal = .2f,
                        configuration = AvatarConfiguration(),
                        seed = "garden"
                    )
                }
            }
        )

        Column(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 24.dp, start = 12.dp, bottom = 8.dp)
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
                            animate = false
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

            AnimatedGarden(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .requiredWidthIn(max = 250.dp),
                configuration = AvatarConfiguration(),
                seed = "garden"
            )
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