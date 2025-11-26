package augmy.interactive.com.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import augmy.interactive.com.base.theme.Colors
import augmy.interactive.com.base.theme.scalingClickable
import augmy.interactive.com.theme.LocalTheme

@Composable
fun BrandActionButton(
    modifier: Modifier = Modifier,
    text: String,
    onPress: () -> Unit,
    imageVector: ImageVector
) {
    Row(
        modifier = modifier
            .scalingClickable(scaleInto = .9f) {
                onPress()
            }
            .background(
                color = LocalTheme.current.colors.brandMainDark,
                shape = LocalTheme.current.shapes.rectangularActionShape
            )
            .padding(vertical = 4.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(32.dp),
            imageVector = imageVector,
            contentDescription = null,
            tint = Color.White
        )
        Text(
            text = text,
            style = LocalTheme.current.styles.subheading.copy(
                color = Color.White
            )
        )
    }
}

@Composable
fun LoadingHeaderButton(
    modifier: Modifier = Modifier,
    text: String = "",
    contentColor: Color = LocalTheme.current.colors.secondary,
    containerColor: Color = LocalTheme.current.colors.backgroundLight,
    contentPadding: PaddingValues = PaddingValues(
        vertical = 10.dp,
        horizontal = 16.dp
    ),
    showBorder: Boolean = true,
    isEnabled: Boolean = true,
    endImageVector: ImageVector? = null,
    textStyle: TextStyle = LocalTheme.current.styles.category,
    shape: Shape = LocalTheme.current.shapes.rectangularActionShape,
    isLoading: Boolean = false,
    onClick: () -> Unit = {},
    additionalContent: @Composable RowScope.() -> Unit = {}
) {
    HeaderButton(
        modifier = modifier,
        text = text,
        isEnabled = isEnabled,
        onClick = onClick,
        showBorder = showBorder,
        contentPadding = contentPadding,
        shape = shape,
        endImageVector = if (!isLoading && isEnabled) endImageVector else null,
        textStyle = textStyle,
        additionalContent = {
            additionalContent()
            AnimatedVisibility(isLoading) {
                val density = LocalDensity.current

                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .requiredSize(
                            with(density) { LocalTheme.current.styles.category.fontSize.toDp() }
                        ),
                    color = containerColor,
                    trackColor = contentColor
                )
            }
        },
        contentColor = contentColor,
        containerColor = containerColor
    )
}

@Composable
private fun HeaderButton(
    modifier: Modifier = Modifier,
    text: String = "",
    contentColor: Color,
    containerColor: Color,
    showBorder: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(
        vertical = 10.dp,
        horizontal = 16.dp
    ),
    endImageVector: ImageVector? = null,
    additionalContent: @Composable RowScope.() -> Unit = {},
    isEnabled: Boolean = true,
    textStyle: TextStyle = LocalTheme.current.styles.category,
    shape: Shape = LocalTheme.current.shapes.rectangularActionShape,
    onClick: () -> Unit = {}
) {
    val density = LocalDensity.current

    val animContentColor by animateColorAsState(
        when {
            isEnabled -> contentColor
            else -> LocalTheme.current.colors.disabled
        },
        label = "animContentColor"
    )
    val animContainerColor by animateColorAsState(
        when {
            isEnabled -> containerColor
            else -> LocalTheme.current.colors.disabledComponent
        },
        label = "controlColorChange"
    )

    Row(
        modifier = modifier
            .scalingClickable(
                enabled = isEnabled,
                onTap = {
                    if (isEnabled) onClick()
                },
                scaleInto = 0.9f
            )
            .background(
                color = animContainerColor,
                shape = shape
            )
            .then(
                if (showBorder) {
                    Modifier.border(
                        width = .5.dp,
                        color = animContentColor,
                        shape = shape
                    )
                }else Modifier
            )
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (text.isNotEmpty()) {
            Text(
                modifier = Modifier
                    .animateContentSize()
                    .padding(end = 6.dp),
                text = text,
                style = textStyle.copy(color = animContentColor)
            )
        }
        androidx.compose.animation.AnimatedVisibility(endImageVector != null) {
            Icon(
                modifier = Modifier
                    .padding(start = if (text.isNotEmpty()) 4.dp else 0.dp)
                    .size(
                        with(density) { textStyle.fontSize.toDp() }
                    ),
                imageVector = endImageVector ?: Icons.Outlined.Close,
                contentDescription = null,
                tint = animContentColor
            )
        }
        additionalContent()
    }
}

@Composable
fun ComponentHeaderButton(
    modifier: Modifier = Modifier,
    text: String = "",
    shape: Shape = LocalTheme.current.shapes.rectangularActionShape,
    endImageVector: ImageVector? = null,
    extraContent: @Composable RowScope.() -> Unit = {},
    onClick: () -> Unit = {}
) {
    HeaderButton(
        modifier = modifier,
        text = text,
        shape = shape,
        onClick = onClick,
        additionalContent = extraContent,
        endImageVector = endImageVector,
        contentColor = LocalTheme.current.colors.secondary,
        containerColor = LocalTheme.current.colors.backgroundLight
    )
}

@Composable
fun BrandHeaderButton(
    modifier: Modifier = Modifier,
    text: String = "",
    contentPadding: PaddingValues = PaddingValues(
        vertical = 10.dp,
        horizontal = 16.dp
    ),
    shape: Shape = LocalTheme.current.shapes.rectangularActionShape,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    endImageVector: ImageVector? = null,
    onClick: () -> Unit = {}
) {
    LoadingHeaderButton(
        modifier = modifier,
        text = text,
        isEnabled = isEnabled,
        contentPadding = contentPadding,
        onClick = onClick,
        isLoading = isLoading,
        showBorder = false,
        contentColor = Colors.GrayLight,
        containerColor = if (isLoading) {
            LocalTheme.current.colors.brandMainDark.copy(alpha = 0.4f)
        }else LocalTheme.current.colors.brandMainDark,
        endImageVector = endImageVector,
        shape = shape
    )
}
