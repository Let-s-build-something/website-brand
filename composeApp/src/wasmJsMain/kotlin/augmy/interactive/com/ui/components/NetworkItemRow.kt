package augmy.interactive.com.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import augmy.interactive.com.base.theme.scalingClickable
import augmy.interactive.com.data.MediaIO
import augmy.interactive.com.data.NetworkItemIO
import augmy.interactive.com.theme.LocalTheme
import augmy.interactive.com.theme.SharedColors

@Composable
fun NetworkItemRow(
    modifier: Modifier = Modifier,
    isChecked: Boolean? = null,
    data: NetworkItemIO?,
    avatarSize: Dp = 48.dp,
    highlight: String? = null,
    isSelected: Boolean = false,
    highlightTitle: Boolean = true,
    indicatorColor: Color? = null,
    onAvatarClick: (() -> Unit)? = null,
    avatarContent: @Composable ((Modifier) -> Unit)? = null,
    actions: @Composable () -> Unit = {},
    content: @Composable RowScope.() -> Unit = {}
) {
    Crossfade(
        modifier = modifier,
        targetState = data != null
    ) { isData ->
        if (isData && data != null) {
            ContentLayout(
                indicatorColor = indicatorColor,
                isChecked = isChecked,
                isSelected = isSelected,
                avatarSize = avatarSize,
                highlight = highlight,
                matchTitle = highlightTitle,
                actions = actions,
                onAvatarClick = onAvatarClick,
                data = data,
                avatarContent = avatarContent,
                content = content
            )
        }else {
            ShimmerLayout()
        }
    }
}

@Composable
private fun ContentLayout(
    indicatorColor: Color?,
    isChecked: Boolean?,
    isSelected: Boolean = false,
    matchTitle: Boolean = true,
    highlight: String? = null,
    avatarSize: Dp = 48.dp,
    data: NetworkItemIO,
    onAvatarClick: (() -> Unit)? = null,
    avatarContent: @Composable ((Modifier) -> Unit)? = null,
    content: @Composable RowScope.() -> Unit = {},
    actions: @Composable () -> Unit = {}
) {
    Column(Modifier.animateContentSize()) {
        Row(
            modifier = Modifier
                .animateContentSize()
                .padding(top = 8.dp, bottom = 8.dp, end = 4.dp)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val avatarModifier = Modifier
                .padding(start = LocalTheme.current.shapes.betweenItemsSpace)
                .size(avatarSize)

            Box {
                avatarContent?.invoke(avatarModifier) ?: AvatarImage(
                    modifier = avatarModifier.scalingClickable(
                        key = data.id,
                        enabled = onAvatarClick != null
                    ) {
                        onAvatarClick?.invoke()
                    },
                    media = data.avatar ?: data.avatarUrl?.let { MediaIO(url = it) },
                    tag = data.tag,
                    name = data.displayName
                )


                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(14.dp)
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

            Column(
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .padding(start = LocalTheme.current.shapes.betweenItemsSpace)
                    .padding(vertical = 4.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = AnnotatedString(data.displayName ?: ""),
                    style = LocalTheme.current.styles.category.let {
                        if (!matchTitle && highlight != null) {
                            it.copy(color = it.color.copy(alpha = 0.4f))
                        } else it
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (data.lastMessage != null) {
                    Text(
                        text = buildAnnotatedLinkString(
                            text = data.lastMessage,
                            onLinkClicked = {}
                        ),
                        style = LocalTheme.current.styles.regular.copy(
                            color = LocalTheme.current.colors.secondary
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            AnimatedVisibility(isChecked == true) {
                Icon(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(32.dp),
                    imageVector = Icons.Filled.Check,
                    tint = LocalTheme.current.colors.secondary,
                    contentDescription = null
                )
            }
            content()
        }
    }
}

@Composable
private fun ShimmerLayout(modifier: Modifier = Modifier) {

}
