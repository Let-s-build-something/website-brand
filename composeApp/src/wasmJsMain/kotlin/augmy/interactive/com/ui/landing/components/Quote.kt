package augmy.interactive.com.ui.landing.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FormatQuote
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import augmy.interactive.com.theme.LocalTheme

@Composable
fun Quote(
    modifier: Modifier = Modifier,
    showBackground: Boolean = true,
    author: String,
    text: String,
) {
    Column(modifier) {
        Row(
            modifier = if (showBackground) {
                Modifier
                    .background(
                        color = LocalTheme.current.colors.backgroundDark.copy(alpha = .7f),
                        shape = LocalTheme.current.shapes.componentShape
                    )
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            }else Modifier
        ) {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .rotate(180f),
                imageVector = Icons.Outlined.FormatQuote,
                tint = LocalTheme.current.colors.secondary,
                contentDescription = null
            )

            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 6.dp)
                    .align(Alignment.CenterVertically)
                    .weight(1f, fill = false),
                text = text,
                style = LocalTheme.current.styles.regular.copy(
                    textAlign = TextAlign.Center
                )
            )

            Icon(
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .size(24.dp),
                imageVector = Icons.Outlined.FormatQuote,
                tint = LocalTheme.current.colors.secondary,
                contentDescription = null
            )
        }
        Text(
            modifier = Modifier
                .padding(top = 2.dp, bottom = 6.dp)
                .align(Alignment.End),
            text = author,
            style = LocalTheme.current.styles.regular.copy(
                fontSize = 16.sp
            )
        )
    }
}