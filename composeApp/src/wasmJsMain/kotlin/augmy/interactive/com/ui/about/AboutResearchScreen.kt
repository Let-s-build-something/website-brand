package augmy.interactive.com.ui.about

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import augmy.interactive.com.base.LocalContentSizeDp
import augmy.interactive.com.base.LocalDeviceType
import augmy.interactive.com.base.ModalScreenContent
import augmy.interactive.com.data.Asset
import augmy.interactive.com.theme.LocalTheme
import augmy.interactive.com.ui.components.AsyncImageThumbnail
import org.jetbrains.compose.resources.stringResource
import website_brand.composeapp.generated.resources.Res
import website_brand.composeapp.generated.resources.about_research_platform_heading
import website_brand.composeapp.generated.resources.about_research_platform_preamble
import website_brand.composeapp.generated.resources.about_research_statement_goals_0
import website_brand.composeapp.generated.resources.about_research_statement_goals_0_content
import website_brand.composeapp.generated.resources.about_research_statement_goals_1
import website_brand.composeapp.generated.resources.about_research_statement_goals_1_content
import website_brand.composeapp.generated.resources.about_research_statement_goals_2
import website_brand.composeapp.generated.resources.about_research_statement_goals_2_content
import website_brand.composeapp.generated.resources.about_research_statement_goals_heading
import website_brand.composeapp.generated.resources.about_research_statement_heading
import website_brand.composeapp.generated.resources.about_research_statement_preamble
import website_brand.composeapp.generated.resources.about_research_summary
import website_brand.composeapp.generated.resources.toolbar_action_about_research

/** home/landing screen which is initially shown on the application */
@Composable
fun AboutResearchScreen() {
    val verticalPadding = (LocalContentSizeDp.current.height / 8).dp
    val horizontalPadding = (LocalContentSizeDp.current.width / 20).dp

    ModalScreenContent(scrollState = rememberScrollState()) {
        SelectionContainer {
            Crossfade(LocalDeviceType.current == WindowWidthSizeClass.Compact) { isCompact ->
                Column(modifier = Modifier.padding(top = 24.dp, start = 12.dp, end = 12.dp)) {
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
        }
    }
}

@Composable
private fun ColumnScope.CompactLayout(verticalPadding: Dp) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(Res.string.toolbar_action_about_research),
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
                asset = Asset.Image.Experiment
            )
        }
        Text(
            modifier = Modifier.padding(top = 32.dp, start = 12.dp),
            text = stringResource(Res.string.about_research_summary),
            style = LocalTheme.current.styles.regular
        )
    }

    Spacer(Modifier.height(verticalPadding))

    VerticalContent(verticalPadding = verticalPadding)
}

@Composable
private fun ColumnScope.LargeLayout(
    horizontalPadding: Dp,
    verticalPadding: Dp
) {
    Row(
        modifier = Modifier.padding(top = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(horizontalPadding)
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = stringResource(Res.string.toolbar_action_about_research),
                style = LocalTheme.current.styles.heading
            )
            Text(
                modifier = Modifier.padding(top = 32.dp, start = 12.dp),
                text = stringResource(Res.string.about_research_summary),
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
                asset = Asset.Image.Experiment
            )
        }
    }

    Spacer(Modifier.height(verticalPadding))

    VerticalContent(fraction = .75f, verticalPadding)
}

@Composable
private fun ColumnScope.VerticalContent(
    fraction: Float = 1f,
    verticalPadding: Dp
) {
    Column(modifier = Modifier.fillMaxWidth(fraction)) {
        Text(
            text = stringResource(Res.string.about_research_platform_heading),
            style = LocalTheme.current.styles.subheading
        )
        Text(
            modifier = Modifier
                .padding(top = 8.dp, start = 12.dp)
                .fillMaxWidth(),
            text = stringResource(Res.string.about_research_platform_preamble),
            style = LocalTheme.current.styles.regular
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(fraction)
            .padding(top = verticalPadding)
    ) {
        Text(
            text = stringResource(Res.string.about_research_statement_heading),
            style = LocalTheme.current.styles.subheading
        )
        Text(
            modifier = Modifier
                .padding(top = 8.dp, start = 12.dp)
                .fillMaxWidth(),
            text = stringResource(Res.string.about_research_statement_preamble),
            style = LocalTheme.current.styles.regular
        )
    }
    Text(
        modifier = Modifier
            .fillMaxWidth(fraction)
            .padding(vertical = verticalPadding / 2),
        text = stringResource(Res.string.about_research_statement_goals_heading),
        style = LocalTheme.current.styles.category
            .copy(color = LocalTheme.current.colors.primary)
    )
    listOf(
        Res.string.about_research_statement_goals_0 to Res.string.about_research_statement_goals_0_content,
        Res.string.about_research_statement_goals_1 to Res.string.about_research_statement_goals_1_content,
        Res.string.about_research_statement_goals_2 to Res.string.about_research_statement_goals_2_content,
    ).forEach { texts ->
        Column(
            modifier = Modifier
                .fillMaxWidth(fraction)
                .padding(
                    start = 8.dp,
                    bottom = verticalPadding / 2
                )
                .align(Alignment.End)
        ) {
            Text(
                text = stringResource(texts.first),
                style = LocalTheme.current.styles.subheading
            )
            Text(
                modifier = Modifier
                    .padding(top = 8.dp, start = 12.dp)
                    .fillMaxWidth(),
                text = stringResource(texts.second),
                style = LocalTheme.current.styles.regular
            )
        }
    }

    Spacer(Modifier.height(verticalPadding))

    GetInTouchText(modifier = Modifier.align(Alignment.CenterHorizontally))

    Spacer(Modifier.height(verticalPadding))
}
