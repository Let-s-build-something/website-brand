package augmy.interactive.com.ui.about

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import augmy.interactive.com.base.LocalContentSizeDp
import augmy.interactive.com.base.LocalDeviceType
import augmy.interactive.com.data.Asset
import augmy.interactive.com.ui.components.AsyncImageThumbnail
import augmy.interactive.com.ui.components.SelectableText
import augmy.interactive.com.theme.LocalTheme
import org.jetbrains.compose.resources.stringResource
import website_brand.composeapp.generated.resources.Res
import website_brand.composeapp.generated.resources.about_research_content
import website_brand.composeapp.generated.resources.toolbar_action_about_research

/** home/landing screen which is initially shown on the application */
@Composable
fun AboutResearchScreen() {
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
private fun CompactLayout(
    verticalPadding: Dp
) {
    Column(
        modifier = Modifier.padding(top = 24.dp, start = 12.dp, end = 12.dp),
        verticalArrangement = Arrangement.spacedBy(verticalPadding)
    ) {
        Box(
            Modifier
                .padding(top = verticalPadding)
                .background(
                    LocalTheme.current.colors.brandMainDark,
                    LocalTheme.current.shapes.roundShape
                )
                .padding(verticalPadding / 7)
        ) {
            AsyncImageThumbnail(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(LocalTheme.current.shapes.componentShape),
                thumbnail = Asset.Image.Experiment.placeholder,
                url = Asset.Image.Experiment.url
            )
        }

        Column(Modifier.fillMaxWidth()) {
            SelectableText(
                text = stringResource(Res.string.toolbar_action_about_research),
                style = LocalTheme.current.styles.heading
            )
            SelectableText(
                modifier = Modifier.padding(top = 32.dp),
                text = stringResource(Res.string.about_research_content),
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
    Row(
        modifier = Modifier.padding(top = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(horizontalPadding)
    ) {
        Column(Modifier.weight(1f)) {
            SelectableText(
                text = stringResource(Res.string.toolbar_action_about_research),
                style = LocalTheme.current.styles.heading
            )
            SelectableText(
                modifier = Modifier.padding(top = 32.dp),
                text = stringResource(Res.string.about_research_content),
                style = LocalTheme.current.styles.regular
            )
        }
        Box(
            Modifier
                .weight(1f)
                .padding(top = 32.dp)
                .background(
                    LocalTheme.current.colors.brandMainDark,
                    LocalTheme.current.shapes.roundShape
                )
                .padding(verticalPadding / 7)
        ) {
            AsyncImageThumbnail(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(LocalTheme.current.shapes.componentShape),
                thumbnail = Asset.Image.Experiment.placeholder,
                url = Asset.Image.Experiment.url
            )
        }
    }
}