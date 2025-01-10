package augmy.interactive.com.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import augmy.interactive.com.data.Asset
import coil3.PlatformContext
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.svg.SvgDecoder

@Composable
fun AsyncSvgImage(
    modifier: Modifier = Modifier,
    asset: Asset,
    tint: Color? = null,
    contentDescription: String? = null
) {
    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(PlatformContext.INSTANCE)
            .data(asset.url)
            .crossfade(true)
            .decoderFactory(SvgDecoder.Factory())
            .build(),
        contentDescription = contentDescription,
        colorFilter = if(tint != null) ColorFilter.tint(tint) else null
    )
}