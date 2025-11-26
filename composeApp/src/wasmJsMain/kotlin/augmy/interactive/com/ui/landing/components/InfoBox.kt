package augmy.interactive.com.ui.landing.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import augmy.interactive.com.theme.LocalTheme
import augmy.interactive.com.ui.components.AutoResizeText
import augmy.interactive.com.ui.components.FontSizeRange

@Composable
fun InfoBox(
    modifier: Modifier = Modifier,
    text: String,
    paddingValues: PaddingValues = PaddingValues(
        vertical = 18.dp,
        horizontal = 12.dp
    ),
    boxColor: Color = LocalTheme.current.colors.disabledComponent,
    style: TextStyle = LocalTheme.current.styles.regular,
    prefixContent: (@Composable () -> Unit)? = {
        Icon(
            modifier = Modifier.size(32.dp),
            imageVector = Icons.Outlined.Lightbulb,
            contentDescription = null,
            tint = style.color
        )
    },
    suffixContent: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = modifier
            .padding(vertical = 4.dp)
            .wrapContentWidth()
            .background(
                color = boxColor,
                shape = LocalTheme.current.shapes.rectangularActionShape
            )
            .padding(paddingValues),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        prefixContent?.invoke()
        SelectionContainer(modifier = Modifier.weight(1f)) {
            AutoResizeText(
                modifier = Modifier.padding(start = if (prefixContent != null) 8.dp else 0.dp),
                text = text,
                style = style,
                fontSizeRange = FontSizeRange(
                    min = 2.sp,
                    max = style.fontSize * 1.5f
                )
            )
        }
        suffixContent?.invoke()
    }
}
