package augmy.interactive.com.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
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