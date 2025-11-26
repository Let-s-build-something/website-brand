package augmy.interactive.com.ui.landing.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import augmy.interactive.com.base.theme.scalingClickable
import augmy.interactive.com.theme.LocalTheme

/**
 * Clickable basic Icon with vector image with minimalistic size
 */
@Composable
fun MinimalisticIcon(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    tint: Color = LocalTheme.current.colors.primary,
    contentDescription: String? = null,
    onTap: ((Offset) -> Unit)? = null
) {
    Icon(
        modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .then(if (onTap != null) {
                modifier.scalingClickable(onTap = onTap)
            }else modifier)
            .padding(4.dp),
        imageVector = imageVector,
        contentDescription = contentDescription,
        tint = tint
    )
}
