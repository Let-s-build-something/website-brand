package augmy.interactive.com.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun BulletText(
    modifier: Modifier = Modifier,
    prefix: String? = "\u2022",
    text: String,
    style: TextStyle
) {
    Row(
        modifier = modifier.padding(vertical = 4.dp),
    ) {
        AnimatedVisibility(
            modifier = Modifier.padding(end = 8.dp),
            visible = prefix != null
        ) {
            Text(
                text = prefix ?: "",
                style = style
            )
        }
        Text(
            text = text,
            style = style
        )
    }
}

@Composable
fun BulletText(
    modifier: Modifier = Modifier,
    prefix: String? = "\u2022",
    text: AnnotatedString,
    style: TextStyle
) {
    Row(
        modifier = modifier.padding(vertical = 4.dp),
    ) {
        AnimatedVisibility(
            modifier = Modifier.padding(end = 8.dp),
            visible = prefix != null
        ) {
            Text(
                text = prefix ?: "",
                style = style
            )
        }
        Text(
            text = text,
            style = style
        )
    }
}