package augmy.interactive.com.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import augmy.interactive.com.base.theme.Colors
import augmy.interactive.com.base.theme.scalingClickable
import augmy.interactive.com.theme.LocalTheme

@Composable
fun ToggleButton(
    modifier: Modifier = Modifier,
    text: AnnotatedString,
    paddingValues: PaddingValues = PaddingValues(
        vertical = 10.dp,
        horizontal = 12.dp
    ),
    isSelected: Boolean,
    backgroundColor: Color,
    borderColor: Color? = null,
    style: TextStyle = LocalTheme.current.styles.category,
    key: Any? = null,
    textColor: Color = if (backgroundColor.luminance() > .5f) Colors.Coffee else Colors.GrayLight,
    onClick: () -> Unit
) {
    val alpha = animateFloatAsState(targetValue = if (isSelected) 1f else 0.6f)
    val shapeRadius = animateDpAsState(
        targetValue = if (isSelected) {
            10.dp
        } else LocalTheme.current.shapes.componentCornerRadius
    )

    Box(
        modifier = modifier
            .scalingClickable(
                key = key,
                scaleInto = .95f
            ) {
                onClick()
            }
            .alpha(alpha.value)
            .then(
                borderColor?.let {
                    Modifier.border(
                        width = 1.dp,
                        color = it,
                        shape = RoundedCornerShape(shapeRadius.value)
                    )
                } ?: Modifier
            )
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(shapeRadius.value)
            )
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        AutoResizeText(
            text = text,
            fontSizeRange = FontSizeRange(
                min = 6.sp,
                max = style.fontSize
            ),
            style = style.copy(
                color = textColor,
                textAlign = TextAlign.Center
            ),
            maxLines = 1,
            overflow = TextOverflow.Clip
        )
    }
}
