package augmy.interactive.com.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import augmy.interactive.com.theme.LocalTheme
import augmy.interactive.com.ui.components.AsyncImageThumbnail
import augmy.interactive.com.ui.components.SelectableText
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.Url
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import org.jetbrains.compose.resources.stringResource
import website_brand.composeapp.generated.resources.Res
import website_brand.composeapp.generated.resources.landing_block_0_content
import website_brand.composeapp.generated.resources.landing_block_0_heading
import website_brand.composeapp.generated.resources.landing_block_1_content
import website_brand.composeapp.generated.resources.landing_block_1_heading

/** home/landing screen which is initially shown on the application */
@Composable
fun LandingScreen() {
    val verticalPadding = (LocalContentSizeDp.current.height / 8).dp
    val horizontalPadding = (LocalContentSizeDp.current.width / 20).dp

    Crossfade(LocalDeviceType.current == WindowWidthSizeClass.Compact) { isCompact ->
        if(isCompact) {
            CompactLayout(verticalPadding = verticalPadding)
        }else {
            LargeLayout(
                verticalPadding = verticalPadding,
                horizontalPadding = horizontalPadding
            )
        }
    }
}

@Composable
private fun CompactLayout(verticalPadding: Dp) {
    Column(modifier = Modifier.padding(horizontal = 12.dp)) {

        Spacer(Modifier.height(verticalPadding))

        // first block
        Box(
            Modifier
                .fillMaxWidth()
                .background(
                    LocalTheme.current.colors.tetrial,
                    LocalTheme.current.shapes.roundShape
                )
                .padding(verticalPadding / 7)
        ) {
            AsyncImageThumbnail(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(LocalTheme.current.shapes.componentShape),
                thumbnail = Asset.Image.NaturePalette.placeholder,
                url = Asset.Image.NaturePalette.url
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
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

        Spacer(Modifier.height(verticalPadding))

        // second block
        val composition by rememberLottieComposition {
            LottieCompositionSpec.Url("https://lottie.host/a87d8352-bc1b-4e58-842e-e5996a23f68e/jVlzK4mG3F.json")
        }

        Image(
            modifier = Modifier
                .sizeIn(maxHeight = 300.dp, maxWidth = 300.dp)
                .fillMaxWidth()
                .aspectRatio(1f, matchHeightConstraintsFirst = true)
                .background(
                    LocalTheme.current.colors.brandMain,
                    LocalTheme.current.shapes.componentShape
                ),
            painter = rememberLottiePainter(
                composition = composition,
                iterations = Int.MAX_VALUE
            ),
            contentDescription = null
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
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
}

@Composable
private fun LargeLayout(
    horizontalPadding: Dp,
    verticalPadding: Dp
) {
    Column(verticalArrangement = Arrangement.spacedBy(verticalPadding)) {
        Spacer(Modifier)
        Row(horizontalArrangement = Arrangement.spacedBy(horizontalPadding)) {
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
                        .fillMaxWidth()
                        .clip(LocalTheme.current.shapes.componentShape),
                    thumbnail = Asset.Image.NaturePalette.placeholder,
                    url = Asset.Image.NaturePalette.url
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(horizontalPadding)) {
            val composition by rememberLottieComposition {
                LottieCompositionSpec.Url("https://lottie.host/a87d8352-bc1b-4e58-842e-e5996a23f68e/jVlzK4mG3F.json")
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
                    iterations = Int.MAX_VALUE
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