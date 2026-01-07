package augmy.interactive.com.shared

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type

object Utils {

    fun Modifier.onEnter(
        enabled: Boolean = true,
        onEnter: (shiftPressed: Boolean) -> Unit
    ): Modifier = if (!enabled) this else this.onPreviewKeyEvent { event ->
        if (event.type == KeyEventType.KeyDown && event.key == Key.Enter) {
            onEnter(event.isShiftPressed)
            true
        } else false
    }

}
