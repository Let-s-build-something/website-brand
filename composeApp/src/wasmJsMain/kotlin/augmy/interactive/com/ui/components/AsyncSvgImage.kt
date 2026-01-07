package augmy.interactive.com.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import augmy.interactive.com.theme.LocalTheme
import coil3.PlatformContext
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter.State
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.svg.SvgDecoder

@Composable
fun AsyncSvgImage(
    modifier: Modifier = Modifier,
    model: Any?,
    tint: Color? = null,
    contentScale: ContentScale = ContentScale.FillWidth,
    contentDescription: String? = null,
    fallbackContent: @Composable (() -> Unit)? = null
) {
    val state = remember(model) {
        mutableStateOf<State?>(null)
    }

    Box(contentAlignment = Alignment.Center) {
        AsyncImage(
            modifier = modifier,
            model = ImageRequest.Builder(PlatformContext.INSTANCE)
                .data(model)
                .crossfade(true)
                .decoderFactory(SvgDecoder.Factory())
                .build(),
            onState = {
                state.value = it
            },
            contentDescription = contentDescription,
            contentScale = contentScale,
            alignment = Alignment.CenterStart,
            colorFilter = if(tint != null) ColorFilter.tint(tint) else null
        )
        AnimatedVisibility(state.value is State.Loading || state.value is State.Error) {
            fallbackContent ?: CircularProgressIndicator(
                modifier = Modifier
                    .zIndex(1f)
                    .requiredSize(18.dp),
                color = LocalTheme.current.colors.brandMainDark,
                trackColor = LocalTheme.current.colors.tertial
            )
        }
    }
}
