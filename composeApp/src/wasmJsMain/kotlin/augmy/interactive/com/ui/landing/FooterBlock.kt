package augmy.interactive.com.ui.landing

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import augmy.interactive.com.base.LocalNavController
import augmy.interactive.com.navigation.NavigationNode
import augmy.interactive.com.theme.LocalTheme
import augmy.interactive.com.ui.components.ComponentHeaderButton
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.Url
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import org.jetbrains.compose.resources.stringResource
import website_brand.composeapp.generated.resources.Res
import website_brand.composeapp.generated.resources.landing_block_3_content
import website_brand.composeapp.generated.resources.landing_block_3_heading
import website_brand.composeapp.generated.resources.landing_summary_action
import website_brand.composeapp.generated.resources.landing_summary_description
import website_brand.composeapp.generated.resources.landing_summary_heading

@Composable
internal fun FooterBlock(verticalPadding: Dp) {
    val navController = LocalNavController.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SelectionContainer {
            Text(
                text = stringResource(Res.string.landing_summary_heading),
                style = LocalTheme.current.styles.heading.copy(textAlign = TextAlign.Center)
            )
        }
        SelectionContainer {
            Text(
                modifier = Modifier
                    .widthIn(max = 700.dp)
                    .fillMaxWidth(),
                text = stringResource(Res.string.landing_summary_description),
                style = LocalTheme.current.styles.regular.copy(textAlign = TextAlign.Center)
            )
        }
        ComponentHeaderButton(
            modifier = Modifier.padding(top = 8.dp),
            text = stringResource(Res.string.landing_summary_action)
        ) {
            navController?.navigate(NavigationNode.Faq.route)
        }

        Spacer(Modifier.height(verticalPadding * 2f))

        SelectionContainer {
            Text(
                text = stringResource(Res.string.landing_block_3_heading),
                style = LocalTheme.current.styles.heading.copy(textAlign = TextAlign.Center)
            )
        }
        SelectionContainer {
            Text(
                modifier = Modifier
                    .widthIn(max = 700.dp)
                    .fillMaxWidth(),
                text = stringResource(Res.string.landing_block_3_content),
                style = LocalTheme.current.styles.regular.copy(textAlign = TextAlign.Center)
            )
        }

        val composition by rememberLottieComposition {
            LottieCompositionSpec.Url("https://lottie.host/93c91fb8-636b-448f-8e08-401564eb07e9/Zs9LeGKFON.lottie")
        }

        Image(
            modifier = Modifier
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
}
