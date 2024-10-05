package augmy.interactive.com.ui.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import augmy.interactive.com.base.LocalContentSizeDp
import augmy.interactive.com.data.Asset
import augmy.interactive.com.ui.components.AsyncImageThumbnail
import augmy.interactive.shared.ui.theme.LocalTheme

/** home/landing screen which is initially shown on the application */
@Composable
fun ContactsScreen() {
    val verticalPadding = (LocalContentSizeDp.current.height / 8).dp

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            Modifier
                .fillMaxWidth(0.5f)
                .background(
                    LocalTheme.current.colors.tetrial,
                    LocalTheme.current.shapes.roundShape
                )
                .padding(verticalPadding / 7)
        ) {
            AsyncImageThumbnail(
                modifier = Modifier
                    .clip(LocalTheme.current.shapes.componentShape),
                thumbnail = Asset.Image.Cooperation.placeholder,
                url = Asset.Image.Cooperation.url
            )
        }
    }
}