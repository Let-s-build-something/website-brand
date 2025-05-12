package augmy.interactive.com.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import augmy.interactive.com.data.Asset
import augmy.interactive.com.theme.LocalTheme
import kotlinx.browser.window
import org.jetbrains.compose.resources.stringResource
import website_brand.composeapp.generated.resources.Res
import website_brand.composeapp.generated.resources.accessibility_play_video

@Composable
fun YoutubeVideoThumbnail(
    modifier: Modifier = Modifier,
    link: String,
    title: String? = null,
    asset: Asset.Image,
    onClick: (String) -> Unit = {
        window.open(it)
    }
) {
    Box(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .clickable {
                onClick(link)
            }
    ) {
        if(!title.isNullOrBlank()) {
            Text(
                modifier = Modifier
                    .zIndex(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                text = title,
                style = LocalTheme.current.styles.regular.copy(color = Color.White)
            )
        }
        AsyncSvgImage(
            modifier = Modifier
                .zIndex(1f)
                .heightIn(min = 32.dp)
                .widthIn(min = 32.dp)
                .fillMaxHeight(.1f)
                .align(Alignment.Center),
            model = Asset.Logo.Youtube.url,
            contentDescription = stringResource(Res.string.accessibility_play_video)
        )
        AsyncImageThumbnail(
            modifier = Modifier
                .heightIn(min = 100.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(LocalTheme.current.shapes.componentShape),
            asset = asset
        )
    }
}