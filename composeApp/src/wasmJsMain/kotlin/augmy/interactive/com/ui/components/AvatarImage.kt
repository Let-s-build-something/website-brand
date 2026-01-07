package augmy.interactive.com.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import augmy.interactive.com.base.theme.Colors
import augmy.interactive.com.data.MediaIO
import augmy.interactive.com.theme.LocalTheme

@Composable
fun AvatarImage(
    modifier: Modifier = Modifier,
    shape: Shape = LocalTheme.current.shapes.componentShape,
    media: MediaIO?,
    name: String?,
    tag: String?,
    animate: Boolean = false,
    contentDescription: String? = null
) {
    Crossfade(
        modifier = modifier,
        targetState = media != null || name != null
    ) { hasImage ->
        if (hasImage) {
            ContentLayout(
                media = media,
                tag = tag,
                name = name,
                animate = animate && tag != null,
                contentDescription = contentDescription,
                shape = shape
            )
        }else {
            ShimmerLayout(shape = shape)
        }
    }
}

@Composable
private fun ShimmerLayout(
    modifier: Modifier = Modifier,
    shape: Shape
) {
}

@Composable
private fun ContentLayout(
    modifier: Modifier = Modifier,
    media: MediaIO?,
    tag: String?,
    name: String?,
    animate: Boolean = false,
    contentDescription: String? = null,
    shape: Shape
) {
    if (animate) {
        val density = LocalDensity.current
        val avatarSize = remember(media) {
            mutableStateOf(0f)
        }
        val infiniteTransition = rememberInfiniteTransition(label = "infiniteScaleBackground")
        val liveScaleBackground by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.15f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = 7000
                    1.15f at 2500 using LinearEasing // Takes 2.5 seconds to reach 1.15f
                    1f at 7000 using LinearEasing // Takes 4.5 seconds to return to 1f
                },
                repeatMode = RepeatMode.Restart
            ),
            label = "liveScaleBackground"
        )

        Box(
            modifier = modifier
                .height(IntrinsicSize.Max)
                .width(IntrinsicSize.Max)
                .animateContentSize()
        ) {
            Box(
                Modifier
                    .padding(
                        avatarSize.value.dp * .15f / 2f
                    )
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .scale(liveScaleBackground)
                    .background(
                        color = tagToColor(tag) ?: LocalTheme.current.colors.tertial,
                        shape = shape
                    )
            )
            ContentElement(
                modifier = modifier
                    .padding(
                        avatarSize.value.dp * .15f / 2f
                    )
                    .fillMaxWidth()
                    .onSizeChanged {
                        if (avatarSize.value == 0f) {
                            avatarSize.value = with(density) {
                                it.width.toDp().value
                            }
                        }
                    },
                contentDescription = contentDescription,
                media = media,
                name = name,
                tag = tag,
                shape = shape
            )
        }
    }else if (tag != null) {
        Box(
            modifier = modifier
                .background(
                    color = tagToColor(tag) ?: LocalTheme.current.colors.tertial,
                    shape = shape
                )
                .height(IntrinsicSize.Max)
                .width(IntrinsicSize.Max)
        ) {
            ContentElement(
                modifier = modifier.scale(0.95f),
                contentDescription = contentDescription,
                media = media,
                name = name,
                tag = tag,
                shape = shape
            )
        }
    }else {
        ContentElement(
            modifier = modifier,
            contentDescription = contentDescription,
            media = media,
            name = name,
            tag = tag,
            shape = shape
        )
    }
}

fun tagToColor(tag: String?) = if (tag != null) Color(("ff$tag").toLong(16)) else null


@Composable
private fun ContentElement(
    modifier: Modifier = Modifier,
    shape: Shape,
    contentDescription: String?,
    media: MediaIO?,
    tag: String?,
    name: String?
) {
    Crossfade(media?.isEmpty == false) { isValid ->
        if (isValid) {
            AsyncImageThumbnail(
                modifier = modifier
                    .clip(shape)
                    .background(
                        color = LocalTheme.current.colors.backgroundDark,
                        shape = shape
                    )
                    .aspectRatio(1f),
                url = media?.url ?: "",
                thumbnail = media?.thumbnail ?: "",
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop
            )
        }else if (name != null || tag != null) {
            val backgroundColor = tagToColor(tag) ?: LocalTheme.current.colors.tertial
            val textColor = if (backgroundColor.luminance() > .5f) Colors.Coffee else Colors.GrayLight

            Box(
                modifier = modifier
                    .aspectRatio(1f, true)
                    .background(
                        color = backgroundColor,
                        shape = shape
                    )
                    .padding(6.dp),
                contentAlignment = Alignment.Center
            ) {
                AutoResizeText(
                    text = initialsOf(name),
                    style = LocalTheme.current.styles.subheading.copy(color = textColor),
                    fontSizeRange = FontSizeRange(
                        min = 6.sp,
                        max = LocalTheme.current.styles.heading.fontSize * 1.5f
                    )
                )
            }
        }
    }
}

fun initialsOf(displayName: String?): String {
    return displayName?.trim()
        ?.split("""[\s_\-.]+""".toRegex()) // split on space, underscore, hyphen, or dot
        ?.let {
            when {
                it.size >= 2 -> it[0].take(1) + it[1].take(1)
                it.isNotEmpty() -> it[0].take(1)
                else -> ""
            }
        } ?: ""
}
