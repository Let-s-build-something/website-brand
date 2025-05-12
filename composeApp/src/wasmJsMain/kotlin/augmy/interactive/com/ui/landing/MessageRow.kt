package augmy.interactive.com.ui.landing

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animate
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import augmy.interactive.com.data.Asset
import augmy.interactive.com.theme.LocalTheme
import augmy.interactive.com.ui.components.AsyncImageThumbnail
import augmy.interactive.com.ui.components.simulation.buildTempoString
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

@Composable
fun ColumnScope.MessageRow(
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
                    .padding(
                        top = if(hasPrevious) 1.dp else 4.dp,
                        bottom = if(hasNext) 1.dp else 4.dp,
                        start = if(message.isCurrentUser) 16.dp else 1.dp,
                        end = if(message.isCurrentUser) 1.dp else 16.dp
                    ),
                message = message,
                hasPrevious = hasPrevious,
                hasNext = hasNext
            )
        }
    }
}

@Composable
private fun MessageBubble(
    modifier: Modifier = Modifier,
    message: SimulatedMessage,
    hasPrevious: Boolean,
    hasNext: Boolean
) {
    val density = LocalDensity.current
    val contentSize = remember(message.content) {
        mutableStateOf(IntSize.Zero)
    }

    val animatedScale = remember(message.content) { mutableStateOf(1f) }
    val hasFinished = rememberSaveable(message.content) {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        if (message.background != null && !hasFinished.value) {
            delay(400)

            animate(
                initialValue = 1f,
                targetValue = 1f,
                animationSpec = message.background
            ) { value, _ ->
                animatedScale.value = value
            }
            hasFinished.value = true
        }
    }

    val cornerRadius = animatedScale.value
        .minus(1f)
        .coerceIn(0.01f, .15f)
        .div(.15f)
        .times(24f)
        .minus(24f)
        .absoluteValue
        .dp

    val shape = if (message.isCurrentUser) {
        RoundedCornerShape(
            topStart = cornerRadius,
            topEnd = if(hasPrevious) 1.dp else cornerRadius,
            bottomStart = cornerRadius,
            bottomEnd = if (hasNext) 1.dp else cornerRadius
        )
    } else {
        RoundedCornerShape(
            topEnd = cornerRadius,
            topStart = if(hasPrevious) 1.dp else cornerRadius,
            bottomEnd = cornerRadius,
            bottomStart = if (hasNext) 1.dp else cornerRadius
        )
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if(message.isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        if(!message.isCurrentUser) {
            AsyncImageThumbnail(
                modifier = Modifier
                    .size(
                        with(density) {
                            LocalTheme.current.styles.subheading.fontSize.toDp() + 20.dp
                        }
                    )
                    .clip(CircleShape),
                asset = Asset.Image.IAmJustAFight,
                contentScale = ContentScale.FillHeight
            )
        }

        Spacer(Modifier.width(6.dp))

        Box(
            modifier = modifier
                .width(IntrinsicSize.Max)
                .height(IntrinsicSize.Max)
        ) {
            Box(
                modifier = Modifier
                    .padding(
                        vertical = contentSize.value.height.dp * .15f / 2f
                    )
                    .fillMaxSize()
                    .scale(scaleX = 1f, scaleY = animatedScale.value)
                    .background(
                        color = if(message.isCurrentUser) {
                            LocalTheme.current.colors.brandMainDark
                        }else LocalTheme.current.colors.brandMain,
                        shape = shape
                    )
            )
            val content = buildTempoString(
                key = message.hashCode(),
                text = AnnotatedString(message.content),
                timings = message.timings,
                enabled = true,
                style = LocalTheme.current.styles.category.copy(color = Color.White).toSpanStyle(),
                onFinish = {}
            )

            Text(
                modifier = Modifier
                    .padding(
                        vertical = 10.dp,
                        horizontal = 14.dp
                    )
                    .onSizeChanged {
                        contentSize.value = with(density) {
                            IntSize(
                                it.width.toDp().value.toInt(),
                                it.height.toDp().value.toInt()
                            )
                        }
                    },
                text = content,
                style = LocalTheme.current.styles.category.copy(color = Color.White)
            )
        }
    }
}
