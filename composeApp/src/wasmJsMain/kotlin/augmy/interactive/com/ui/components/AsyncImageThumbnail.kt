package augmy.interactive.com.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import augmy.interactive.com.data.Asset
import coil3.PlatformContext
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade

/** Async image intended for larger image loading with low resolution being loaded first */
@Composable
fun AsyncImageThumbnail(
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.FillWidth,
    thumbnail: String,
    url: String
) {
    val loadOriginal = rememberSaveable {
        mutableStateOf(false)
    }
    val displayOriginal = rememberSaveable {
        mutableStateOf(false)
    }

    val thumbnailRequest =  ImageRequest.Builder(PlatformContext.INSTANCE)
        .data(if(displayOriginal.value) url else thumbnail)
        .crossfade(true)
        .listener(
            onSuccess = { _, _ ->
                loadOriginal.value = true
            }
        )
        .build()

    val originalRequest = ImageRequest.Builder(PlatformContext.INSTANCE)
        .data(if(displayOriginal.value) url else thumbnail)
        .crossfade(true)
        .listener(
            onSuccess = { _, _ ->
                displayOriginal.value = true
            }
        )
        .build()

    AsyncImage(
        modifier = modifier.animateContentSize(),
        model = if(displayOriginal.value) originalRequest else thumbnailRequest,
        contentDescription = null,
        contentScale = contentScale
    )
}

@Composable
fun AsyncImageThumbnail(
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.FillWidth,
    asset: Asset.Image
) {
    AsyncImageThumbnail(
        modifier = modifier,
        contentScale = contentScale,
        thumbnail = asset.thumbnail,
        url = asset.url
    )
}