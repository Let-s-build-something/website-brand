package augmy.interactive.com.ui.about

import androidx.compose.animation.Crossfade
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
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import augmy.interactive.com.base.LocalContentSizeDp
import augmy.interactive.com.base.LocalDeviceType
import augmy.interactive.com.base.LocalNavController
import augmy.interactive.com.data.Asset
import augmy.interactive.com.navigation.NavigationNode
import augmy.interactive.com.theme.LocalTheme
import augmy.interactive.com.ui.components.AsyncImageThumbnail
import augmy.interactive.com.ui.components.BulletText
import augmy.interactive.com.ui.components.YoutubeVideoThumbnail
import augmy.interactive.com.ui.components.buildAnnotatedLinkString
import org.jetbrains.compose.resources.stringResource
import website_brand.composeapp.generated.resources.Res
import website_brand.composeapp.generated.resources.about_content
import website_brand.composeapp.generated.resources.about_content_demo
import website_brand.composeapp.generated.resources.about_content_introduction_0
import website_brand.composeapp.generated.resources.about_content_introduction_1
import website_brand.composeapp.generated.resources.about_content_introduction_2
import website_brand.composeapp.generated.resources.about_content_introduction_3
import website_brand.composeapp.generated.resources.about_content_join_us_0
import website_brand.composeapp.generated.resources.about_content_join_us_1
import website_brand.composeapp.generated.resources.about_content_join_us_2
import website_brand.composeapp.generated.resources.about_content_join_us_3
import website_brand.composeapp.generated.resources.about_content_join_us_4
import website_brand.composeapp.generated.resources.about_content_problem
import website_brand.composeapp.generated.resources.about_content_roadmap
import website_brand.composeapp.generated.resources.about_content_roadmap_here
import website_brand.composeapp.generated.resources.about_content_solution_0
import website_brand.composeapp.generated.resources.about_content_solution_1
import website_brand.composeapp.generated.resources.about_content_summary
import website_brand.composeapp.generated.resources.about_header_demo
import website_brand.composeapp.generated.resources.about_header_introduction
import website_brand.composeapp.generated.resources.about_header_join_us
import website_brand.composeapp.generated.resources.about_header_problem
import website_brand.composeapp.generated.resources.about_header_roadmap
import website_brand.composeapp.generated.resources.about_header_solution
import website_brand.composeapp.generated.resources.about_header_summary
import website_brand.composeapp.generated.resources.toolbar_action_about
import website_brand.composeapp.generated.resources.video_heider_simmel_title
import website_brand.composeapp.generated.resources.video_heider_simmel_url

/** Screen with general information about the project for wide public */
@Composable
fun AboutScreen() {
    val verticalPadding = (LocalContentSizeDp.current.height / 8).dp
    val horizontalPadding = (LocalContentSizeDp.current.width / 20).dp

    SelectionContainer {
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
                    LocalTheme.current.colors.brandMain,
                    LocalTheme.current.shapes.roundShape
                )
                .padding(verticalPadding / 7)
        ) {
            AsyncImageThumbnail(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(LocalTheme.current.shapes.componentShape),
                asset = Asset.Image.DesignConstruction
            )
        }

        Column(Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(Res.string.toolbar_action_about),
                style = LocalTheme.current.styles.heading
            )
            Text(
                modifier = Modifier.padding(top = 32.dp, start = 12.dp),
                text = stringResource(Res.string.about_content),
                style = LocalTheme.current.styles.regular
            )
        }

        Column(Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(Res.string.about_header_summary),
                style = LocalTheme.current.styles.heading
            )
            Text(
                modifier = Modifier.padding(top = 32.dp, start = 12.dp),
                text = stringResource(Res.string.about_content_summary),
                style = LocalTheme.current.styles.regular
            )
        }
        VerticalContent()
    }
}

@Composable
private fun LargeLayout(
    horizontalPadding: Dp,
    verticalPadding: Dp
) {
    Column(
        modifier = Modifier.padding(top = 24.dp, start = 12.dp, end = 12.dp),
        verticalArrangement = Arrangement.spacedBy(verticalPadding)
    ) {
        Row(
            modifier = Modifier.padding(top = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(horizontalPadding)
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = stringResource(Res.string.toolbar_action_about),
                    style = LocalTheme.current.styles.heading
                )
                Text(
                    modifier = Modifier.padding(top = 32.dp, start = 12.dp),
                    text = stringResource(Res.string.about_content),
                    style = LocalTheme.current.styles.regular
                )

                Text(
                    modifier = Modifier.padding(top = 32.dp),
                    text = stringResource(Res.string.about_header_summary),
                    style = LocalTheme.current.styles.heading
                )
                Text(
                    modifier = Modifier.padding(top = 32.dp, start = 12.dp),
                    text = stringResource(Res.string.about_content_summary),
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
                    asset = Asset.Image.DesignConstruction
                )
            }
        }

        VerticalContent(fraction = .75f)
    }
}

@Composable
private fun VerticalContent(fraction: Float = 1f) {
    val navController = LocalNavController.current

    Column(Modifier.fillMaxWidth(fraction)) {
        Text(
            text = stringResource(Res.string.about_header_introduction),
            style = LocalTheme.current.styles.heading
        )
        Spacer(Modifier.height(32.dp))
        listOf(
            Res.string.about_content_introduction_0,
            Res.string.about_content_introduction_1,
            Res.string.about_content_introduction_2,
            Res.string.about_content_introduction_3,
        ).forEach {
            BulletText(
                modifier = Modifier.padding(top = 8.dp, start = 12.dp),
                text = stringResource(it),
                style = LocalTheme.current.styles.regular
            )
        }
    }

    Column(Modifier.fillMaxWidth(fraction)) {
        Text(
            text = stringResource(Res.string.about_header_problem),
            style = LocalTheme.current.styles.heading
        )
        Text(
            modifier = Modifier.padding(top = 32.dp, start = 12.dp),
            text = stringResource(Res.string.about_content_problem),
            style = LocalTheme.current.styles.regular
        )
    }

    Column(Modifier.fillMaxWidth(fraction)) {
        Text(
            text = stringResource(Res.string.about_header_solution),
            style = LocalTheme.current.styles.heading
        )

        Text(
            modifier = Modifier.padding(top = 32.dp, start = 12.dp),
            text = stringResource(Res.string.about_content_solution_0),
            style = LocalTheme.current.styles.regular
        )

        YoutubeVideoThumbnail(
            modifier = Modifier
                .fillMaxWidth(fraction)
                .aspectRatio(1.33f)
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 32.dp, horizontal = 12.dp),
            title = stringResource(Res.string.video_heider_simmel_title),
            link = stringResource(Res.string.video_heider_simmel_url),
            asset = Asset.Image.HeiderSimmelPreview
        )
        Text(
            modifier = Modifier.padding(start = 12.dp),
            text = stringResource(Res.string.about_content_solution_1),
            style = LocalTheme.current.styles.regular
        )
    }

    Column(Modifier.fillMaxWidth(fraction)) {
        Text(
            text = stringResource(Res.string.about_header_demo),
            style = LocalTheme.current.styles.heading
        )
        Text(
            modifier = Modifier.padding(top = 32.dp, start = 12.dp),
            text = stringResource(Res.string.about_content_demo),
            style = LocalTheme.current.styles.regular
        )
    }

    Column(Modifier.fillMaxWidth(fraction)) {
        Text(
            text = stringResource(Res.string.about_header_join_us),
            style = LocalTheme.current.styles.heading
        )
        Spacer(Modifier.height(32.dp))
        listOf(
            Res.string.about_content_join_us_0,
            Res.string.about_content_join_us_1,
            Res.string.about_content_join_us_2,
            Res.string.about_content_join_us_3,
            Res.string.about_content_join_us_4,
        ).forEach {
            BulletText(
                modifier = Modifier.padding(top = 8.dp, start = 12.dp),
                text = buildAnnotatedLinkString(stringResource(it)),
                style = LocalTheme.current.styles.regular
            )
        }

        Column(Modifier.fillMaxWidth(fraction)) {
            Text(
                text = stringResource(Res.string.about_header_roadmap),
                style = LocalTheme.current.styles.heading
            )
            Text(
                modifier = Modifier.padding(top = 32.dp, start = 12.dp),
                text = buildAnnotatedString {
                    append(stringResource(Res.string.about_content_roadmap))
                    withLink(
                        link = LinkAnnotation.Clickable(
                            tag = "ACTION",
                            styles = LocalTheme.current.styles.link,
                            linkInteractionListener = {
                                navController?.navigate(NavigationNode.Roadmap.route)
                            },
                        ),
                    ) {
                        append(" " + stringResource(Res.string.about_content_roadmap_here))
                    }
                    append(".")
                },
                style = LocalTheme.current.styles.regular
            )
        }
    }

    Spacer(Modifier)
}