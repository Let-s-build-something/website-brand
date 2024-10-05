package augmy.interactive.com.ui.components

import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle

@Composable
fun SelectableText(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle = LocalTextStyle.current
) {
    SelectionContainer {
        Text(
            modifier = modifier,
            text = text,
            style = style
        )
    }
}

@Composable
fun SelectableText(
    modifier: Modifier = Modifier,
    text: AnnotatedString,
    style: TextStyle = LocalTextStyle.current
) {
    SelectionContainer {
        Text(
            modifier = modifier,
            text = text,
            style = style
        )
    }
}