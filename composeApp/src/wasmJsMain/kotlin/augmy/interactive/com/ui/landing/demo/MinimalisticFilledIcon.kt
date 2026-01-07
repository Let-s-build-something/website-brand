package augmy.interactive.com.ui.landing.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import augmy.interactive.com.base.theme.scalingClickable
import augmy.interactive.com.theme.LocalTheme

@Composable
fun MinimalisticFilledIcon(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    tint: Color = LocalTheme.current.colors.secondary,
    background: Color = LocalTheme.current.colors.backgroundDark,
    contentDescription: String? = null,
    onTap: ((Offset) -> Unit)? = null
) {
    Icon(
        modifier = modifier
            .size(36.dp)
            .then(if (onTap != null) {
                Modifier.scalingClickable(onTap = onTap)
            }else Modifier)
            .background(
                color = background,
                shape = LocalTheme.current.shapes.rectangularActionShape
            )
            .padding(5.dp),
        imageVector = imageVector,
        contentDescription = contentDescription,
        tint = tint
    )
}