package augmy.interactive.com.base.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import augmy.interactive.com.theme.ThemeShapes

/** Styles specific to main app theme [LocalTheme.current] */
class AppThemeShapes: ThemeShapes {
    override val betweenItemsSpace: Dp = 8.dp
    override val iconSizeSmall: Dp = 32.dp
    override val iconSizeMedium: Dp = 48.dp
    override val iconSizeLarge: Dp = 64.dp
    override val componentCornerRadius: Dp = 16.dp
    override val circularActionShape: Shape = CircleShape
    override val rectangularActionShape: Shape = RoundedCornerShape(10.dp)
    override val componentShape: Shape = RoundedCornerShape(componentCornerRadius)
    override val roundShape: Shape = RoundedCornerShape(28.dp)
    override val chipShape: Shape = CircleShape
}