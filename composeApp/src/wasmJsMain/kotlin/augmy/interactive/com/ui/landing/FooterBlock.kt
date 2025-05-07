package augmy.interactive.com.ui.landing

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import augmy.interactive.com.theme.LocalTheme
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.Url
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import org.jetbrains.compose.resources.stringResource
import website_brand.composeapp.generated.resources.Res
import website_brand.composeapp.generated.resources.landing_block_3_content
import website_brand.composeapp.generated.resources.landing_block_3_heading

@Composable
internal fun ColumnScope.FooterBlock(verticalPadding: Dp) {
    Text(
        modifier = Modifier
            .padding(top = verticalPadding)
            .fillMaxWidth(),
        text = stringResource(Res.string.landing_block_3_heading),
        style = LocalTheme.current.styles.heading.copy(textAlign = TextAlign.Center)
    )
    Text(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .widthIn(max = 700.dp)
            .fillMaxWidth(),
        text = stringResource(Res.string.landing_block_3_content),
        style = LocalTheme.current.styles.regular.copy(textAlign = TextAlign.Center)
    )

    val composition by rememberLottieComposition {
        LottieCompositionSpec.Url("https://lottie.host/93c91fb8-636b-448f-8e08-401564eb07e9/Zs9LeGKFON.lottie")
    }

    Image(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .sizeIn(maxHeight = 300.dp, maxWidth = 300.dp)
            .fillMaxWidth(0.8f)
            .aspectRatio(1f, matchHeightConstraintsFirst = true),
        painter = rememberLottiePainter(
            composition = composition,
            iterations = Int.MAX_VALUE,
            speed = 0.25f
        ),
        contentDescription = null
    )
}