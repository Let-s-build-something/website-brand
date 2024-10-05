package augmy.interactive.com.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import augmy.interactive.com.base.LocalContentSizeDp
import augmy.interactive.com.base.LocalDeviceType
import augmy.interactive.com.data.Asset
import augmy.interactive.com.ui.components.AsyncImageThumbnail
import augmy.interactive.com.ui.components.SelectableText
import augmy.interactive.shared.ui.theme.LocalTheme
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import website_brand.composeapp.generated.resources.Res
import website_brand.composeapp.generated.resources.landing_block_0_content
import website_brand.composeapp.generated.resources.landing_block_0_heading
import website_brand.composeapp.generated.resources.landing_block_1_content
import website_brand.composeapp.generated.resources.landing_block_1_heading

/** home/landing screen which is initially shown on the application */
@OptIn(ExperimentalResourceApi::class)
@Composable
fun LandingScreen() {
    val verticalPadding = (LocalContentSizeDp.current.height / 8).dp
    val horizontalPadding = (LocalContentSizeDp.current.width / 20).dp

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(verticalPadding)
    ) {
        Spacer(Modifier)
        ConditionalParent(
            verticalPadding = verticalPadding,
            horizontalPadding = horizontalPadding
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                SelectableText(
                    text = stringResource(Res.string.landing_block_0_heading),
                    style = LocalTheme.current.styles.heading
                )
                SelectableText(
                    text = stringResource(Res.string.landing_block_0_content),
                    style = LocalTheme.current.styles.regular
                )
            }
            Box(
                Modifier
                    .weight(1f)
                    .background(
                        LocalTheme.current.colors.tetrial,
                        LocalTheme.current.shapes.roundShape
                    )
                    .padding(verticalPadding / 7)
            ) {
                AsyncImageThumbnail(
                    modifier = Modifier
                        .clip(LocalTheme.current.shapes.componentShape),
                    thumbnail = Asset.Image.NaturePalette.placeholder,
                    url = Asset.Image.NaturePalette.url
                )
            }
        }
        ConditionalParent(
            verticalPadding = verticalPadding,
            horizontalPadding = horizontalPadding
        ) {
            val composition by rememberLottieComposition {
                LottieCompositionSpec.JsonString(Res.readBytes("files/emoji_thinking.json").decodeToString())
            }

            Image(
                modifier = Modifier
                    .sizeIn(maxHeight = 300.dp, maxWidth = 300.dp)
                    .aspectRatio(1f, matchHeightConstraintsFirst = true)
                    .background(
                        LocalTheme.current.colors.brandMain,
                        LocalTheme.current.shapes.componentShape
                    ),
                painter = rememberLottiePainter(
                    composition = composition,
                    iterations = 100
                ),
                contentDescription = null
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                SelectableText(
                    text = stringResource(Res.string.landing_block_1_heading),
                    style = LocalTheme.current.styles.heading
                )
                SelectableText(
                    text = stringResource(Res.string.landing_block_1_content),
                    style = LocalTheme.current.styles.regular
                )
            }
        }
        Spacer(Modifier)
    }
}

@Composable
private fun ConditionalParent(
    horizontalPadding: Dp,
    verticalPadding: Dp,
    content: @Composable () -> Unit
) {
    if(LocalDeviceType.current == WindowWidthSizeClass.Compact) {
        Column(
            verticalArrangement = Arrangement.spacedBy(verticalPadding),
            content = {
                content()
            }
        )
    }else Row(
        horizontalArrangement = Arrangement.spacedBy(horizontalPadding),
        content = {
            content()
        }
    )
}