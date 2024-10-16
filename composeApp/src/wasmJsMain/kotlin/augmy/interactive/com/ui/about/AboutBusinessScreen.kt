package augmy.interactive.com.ui.about

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import augmy.interactive.com.base.LocalContentSizeDp
import augmy.interactive.com.base.LocalDeviceType
import augmy.interactive.com.base.ModalScreenContent
import augmy.interactive.com.data.Asset
import augmy.interactive.com.theme.LocalTheme
import augmy.interactive.com.ui.components.AsyncImageThumbnail
import augmy.interactive.com.ui.components.buildAnnotatedLinkString
import kotlinx.browser.window
import org.jetbrains.compose.resources.stringResource
import website_brand.composeapp.generated.resources.Res
import website_brand.composeapp.generated.resources.about_business_content
import website_brand.composeapp.generated.resources.toolbar_action_about_business

/** home/landing screen which is initially shown on the application */
@Composable
fun AboutBusinessScreen() {
    val verticalPadding = (LocalContentSizeDp.current.height / 8).dp
    val horizontalPadding = (LocalContentSizeDp.current.width / 20).dp

    ModalScreenContent(scrollState = rememberScrollState()) {
        SelectionContainer {
            Crossfade(LocalDeviceType.current == WindowWidthSizeClass.Compact) { isCompact ->
                if(isCompact) {
                    compactLayout(verticalPadding = verticalPadding)
                }else {
                    largeLayout(
                        verticalPadding = verticalPadding,
                        horizontalPadding = horizontalPadding
                    )
                }
            }
        }
    }
}

@Composable
private fun compactLayout(
    verticalPadding: Dp
) {
    Column(
        modifier = Modifier.padding(top = 24.dp, start = 12.dp, end = 12.dp),
        verticalArrangement = Arrangement.spacedBy(verticalPadding)
    ) {
        Column(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(Res.string.toolbar_action_about_business),
                style = LocalTheme.current.styles.heading
            )
            Box(
                Modifier
                    .background(
                        LocalTheme.current.colors.brandMain,
                        LocalTheme.current.shapes.roundShape
                    )
                    .padding(verticalPadding / 7)
            ) {
                AsyncImageThumbnail(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(LocalTheme.current.shapes.componentShape),
                    asset = Asset.Image.EarHelp
                )
            }
            Text(
                modifier = Modifier.padding(top = 32.dp, start = 12.dp),
                text = buildAnnotatedLinkString(
                    stringResource(Res.string.about_business_content),
                    onLinkClicked = { link ->
                        window.open(link)
                    }
                ),
                style = LocalTheme.current.styles.regular
            )
        }
    }
}

@Composable
private fun largeLayout(
    horizontalPadding: Dp,
    verticalPadding: Dp
) {
    Row(
        modifier = Modifier.padding(top = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(horizontalPadding)
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = stringResource(Res.string.toolbar_action_about_business),
                style = LocalTheme.current.styles.heading
            )
            Text(
                modifier = Modifier.padding(top = 32.dp),
                text = buildAnnotatedLinkString(
                    stringResource(Res.string.about_business_content),
                    onLinkClicked = { link ->
                        window.open(link)
                    }
                ),
                style = LocalTheme.current.styles.regular
            )
        }
        Box(
            Modifier
                .weight(1f)
                .padding(top = 32.dp)
                .background(
                    LocalTheme.current.colors.brandMain,
                    LocalTheme.current.shapes.roundShape
                )
                .padding(verticalPadding / 7)
        ) {
            AsyncImageThumbnail(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(LocalTheme.current.shapes.componentShape),
                thumbnail = Asset.Image.EarHelp.thumbnail,
                url = Asset.Image.EarHelp.url
            )
        }
    }
}