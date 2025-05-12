package augmy.interactive.com.ui.landing

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import augmy.interactive.com.data.Asset
import augmy.interactive.com.theme.LocalTheme
import augmy.interactive.com.ui.components.AsyncImageThumbnail
import augmy.interactive.com.ui.landing.social_circle.SocialCircle
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
import website_brand.composeapp.generated.resources.landing_block_2_content
import website_brand.composeapp.generated.resources.landing_block_2_heading
import website_brand.composeapp.generated.resources.landing_messages_enhanced_text
import website_brand.composeapp.generated.resources.landing_messages_text
import website_brand.composeapp.generated.resources.landing_social_circle_heading
import website_brand.composeapp.generated.resources.landing_social_circle_text

@Composable
internal fun CompactLayout(
    scrollState: ScrollState,
    verticalPadding: Dp
) {
    Column(modifier = Modifier.padding(horizontal = 12.dp)) {
        SocialCircle(
            scrollState = scrollState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        SelectionContainer {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(Res.string.landing_social_circle_heading),
                    style = LocalTheme.current.styles.heading
                )
                Text(
                    text = stringResource(Res.string.landing_social_circle_text),
                    style = LocalTheme.current.styles.regular
                )
            }
        }

        Spacer(Modifier.height(verticalPadding))

        val composition2 by rememberLottieComposition {
            LottieCompositionSpec.Url("https://lottie.host/49fb6653-3dbc-4925-b221-520ee02bd7af/j5WC6v0PFm.lottie")
        }
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .sizeIn(maxHeight = 400.dp, maxWidth = 400.dp)
                .aspectRatio(1f, matchHeightConstraintsFirst = true)
                .background(
                    LocalTheme.current.colors.brandMainDark,
                    LocalTheme.current.shapes.componentShape
                ),
            painter = rememberLottiePainter(
                composition = composition2,
                iterations = Int.MAX_VALUE,
                speed = .75f
            ),
            contentDescription = null
        )
        SelectionContainer {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(Res.string.landing_block_2_heading),
                    style = LocalTheme.current.styles.heading
                )
                Text(
                    text = stringResource(Res.string.landing_block_2_content),
                    style = LocalTheme.current.styles.regular
                )
            }
        }

        Spacer(Modifier.height(verticalPadding * 2))

        MessagesSection(
            scrollState = scrollState,
            verticalContent = { isEnhanced ->
                SelectionContainer {
                    Text(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .animateContentSize()
                            .background(
                                color = LocalTheme.current.colors.backgroundDark,
                                shape = LocalTheme.current.shapes.componentShape
                            )
                            .padding(horizontal = 12.dp, vertical = 24.dp),
                        text = stringResource(
                            if(isEnhanced) Res.string.landing_messages_enhanced_text
                            else Res.string.landing_messages_text
                        ),
                        style = LocalTheme.current.styles.category
                    )
                }
            }
        )

        Spacer(Modifier)


        Box(
            Modifier
                .fillMaxWidth()
                .padding(verticalPadding / 7)
        ) {
            AsyncImageThumbnail(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(LocalTheme.current.shapes.componentShape),
                asset = Asset.Image.NaturePalette
            )
        }
        SelectionContainer {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(Res.string.landing_block_0_heading),
                    style = LocalTheme.current.styles.heading
                )
                Text(
                    text = stringResource(Res.string.landing_block_0_content),
                    style = LocalTheme.current.styles.regular
                )
            }
        }

        Spacer(Modifier.height(verticalPadding))

        val composition by rememberLottieComposition {
            LottieCompositionSpec.Url("https://lottie.host/6dc94a1e-3c98-4f74-a4b3-387c187fc377/iHZAJetUK8.lottie")
        }

        Image(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .sizeIn(maxHeight = 300.dp, maxWidth = 300.dp)
                .fillMaxWidth()
                .aspectRatio(1f, matchHeightConstraintsFirst = true)
                .background(
                    LocalTheme.current.colors.brandMain,
                    LocalTheme.current.shapes.componentShape
                ),
            painter = rememberLottiePainter(
                composition = composition,
                iterations = Int.MAX_VALUE,
                speed = .75f
            ),
            contentDescription = null
        )
        SelectionContainer {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(Res.string.landing_block_1_heading),
                    style = LocalTheme.current.styles.heading
                )
                Text(
                    text = stringResource(Res.string.landing_block_1_content),
                    style = LocalTheme.current.styles.regular
                )
            }
        }
    }
}
