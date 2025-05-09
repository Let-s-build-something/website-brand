package augmy.interactive.com.ui.landing

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import augmy.interactive.com.base.LocalContentSizeDp
import augmy.interactive.com.base.LocalDeviceType
import augmy.interactive.com.base.LocalNavController
import augmy.interactive.com.base.ModalScreenContent
import augmy.interactive.com.navigation.NavigationNode
import augmy.interactive.com.theme.LocalTheme
import augmy.interactive.com.ui.components.IndicatedAction
import org.jetbrains.compose.resources.stringResource
import website_brand.composeapp.generated.resources.Res
import website_brand.composeapp.generated.resources.landing_footer_action
import website_brand.composeapp.generated.resources.landing_footer_content
import website_brand.composeapp.generated.resources.landing_footer_heading
import website_brand.composeapp.generated.resources.landing_header_content
import website_brand.composeapp.generated.resources.landing_header_heading


/** home/landing screen which is initially shown on the application */
@Composable
fun LandingScreen() {
    val navController = LocalNavController.current
    val verticalPadding = (LocalContentSizeDp.current.height / 8).dp
    val horizontalPadding = (LocalContentSizeDp.current.width / 20).dp

    val scrollState = rememberScrollState()


    ModalScreenContent(
        modifier = Modifier.padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        scrollState = scrollState
    ) {
        SelectionContainer {
            Column {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = verticalPadding)
                        .fillMaxWidth(.8f),
                    text = stringResource(Res.string.landing_header_heading),
                    style = LocalTheme.current.styles.heading.copy(textAlign = TextAlign.Center)
                )
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 2.dp)
                        .fillMaxWidth(.8f),
                    text = stringResource(Res.string.landing_header_content),
                    style = LocalTheme.current.styles.regular.copy(textAlign = TextAlign.Center)
                )

                Spacer(Modifier.height(verticalPadding * 2))
            }
        }

        Column {
            Crossfade(LocalDeviceType.current == WindowWidthSizeClass.Compact) { isCompact ->
                if(isCompact) {
                    CompactLayout(
                        verticalPadding = verticalPadding,
                        scrollState = scrollState
                    )
                }else {
                    LargeLayout(
                        verticalPadding = verticalPadding,
                        horizontalPadding = horizontalPadding,
                        scrollState = scrollState
                    )
                }
            }

            Spacer(Modifier.height(verticalPadding))
            FooterBlock(verticalPadding)
            Spacer(Modifier.height(verticalPadding))
        }

        DownloadBlock()

        Spacer(Modifier.height(verticalPadding))

        SelectionContainer {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(.8f)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.landing_footer_heading),
                    style = LocalTheme.current.styles.heading.copy(textAlign = TextAlign.Center)
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.landing_footer_content),
                    style = LocalTheme.current.styles.regular.copy(textAlign = TextAlign.Center)
                )
                IndicatedAction(
                    modifier = Modifier.align(Alignment.End),
                    content = { modifier ->
                        Text(
                            modifier = modifier,
                            text = stringResource(Res.string.landing_footer_action),
                            style = LocalTheme.current.styles.regular
                        )
                    },
                    onPress = {
                        navController?.navigate(NavigationNode.PublicAbout.route)
                    }
                )
            }
        }

        Spacer(Modifier.height(verticalPadding))
    }
}
