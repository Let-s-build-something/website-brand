package augmy.interactive.com.ui.about

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import augmy.interactive.com.base.LocalContentSizeDp
import augmy.interactive.com.base.LocalDeviceType
import augmy.interactive.com.base.ModalScreenContent
import augmy.interactive.com.data.Asset
import augmy.interactive.com.theme.LocalTheme
import augmy.interactive.com.ui.components.AsyncImageThumbnail
import augmy.interactive.com.ui.components.buildAnnotatedLink
import augmy.interactive.com.ui.components.buildAnnotatedLinkString
import kotlinx.browser.window
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import website_brand.composeapp.generated.resources.Res
import website_brand.composeapp.generated.resources.about_business_footer
import website_brand.composeapp.generated.resources.about_business_footer_link_0
import website_brand.composeapp.generated.resources.about_business_footer_link_0_url
import website_brand.composeapp.generated.resources.about_business_footer_link_1
import website_brand.composeapp.generated.resources.about_business_footer_link_1_url
import website_brand.composeapp.generated.resources.about_business_heading
import website_brand.composeapp.generated.resources.about_business_list_0
import website_brand.composeapp.generated.resources.about_business_list_0_content
import website_brand.composeapp.generated.resources.about_business_list_1
import website_brand.composeapp.generated.resources.about_business_list_1_content
import website_brand.composeapp.generated.resources.about_business_list_2
import website_brand.composeapp.generated.resources.about_business_list_2_content
import website_brand.composeapp.generated.resources.about_business_list_3
import website_brand.composeapp.generated.resources.about_business_list_3_content
import website_brand.composeapp.generated.resources.about_business_list_4
import website_brand.composeapp.generated.resources.about_business_list_4_content
import website_brand.composeapp.generated.resources.about_business_list_5
import website_brand.composeapp.generated.resources.about_business_list_5_content
import website_brand.composeapp.generated.resources.about_business_list_heading
import website_brand.composeapp.generated.resources.about_business_summary

/** home/landing screen which is initially shown on the application */
@Composable
fun AboutBusinessScreen() {
    val verticalPadding = (LocalContentSizeDp.current.height / 8).dp
    val horizontalPadding = (LocalContentSizeDp.current.width / 20).dp

    ModalScreenContent(scrollState = rememberScrollState()) {
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

@Composable
private fun compactLayout(verticalPadding: Dp) {
    SelectionContainer {
        Column(
            modifier = Modifier.padding(top = 24.dp, start = 12.dp, end = 12.dp),
            verticalArrangement = Arrangement.spacedBy(verticalPadding)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(Res.string.about_business_heading),
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
                    modifier = Modifier.padding(bottom = 32.dp, top = 16.dp),
                    text = buildAnnotatedLinkString(
                        stringResource(Res.string.about_business_summary),
                        onLinkClicked = { link ->
                            window.open(link)
                        }
                    ),
                    style = LocalTheme.current.styles.regular
                )
                GetInTouchText()

                Content(verticalPadding, isMobile = true)
            }
            GetInTouchText(modifier = Modifier.align(Alignment.CenterHorizontally))

            Spacer(Modifier.height(verticalPadding))
        }
    }
}

@Composable
fun GetInTouchText(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()

    Text(
        modifier = modifier.padding(top = 8.dp),
        text = buildAnnotatedLink(
            text = stringResource(Res.string.about_business_footer),
            linkTexts = listOf(
                stringResource(Res.string.about_business_footer_link_0),
                stringResource(Res.string.about_business_footer_link_1),
            ),
            onLinkClicked = { _, index ->
                scope.launch {
                    when(index) {
                        0 -> window.open(getString(Res.string.about_business_footer_link_0_url))
                        else -> window.open(getString(Res.string.about_business_footer_link_1_url))
                    }
                }
            }
        ),
        style = LocalTheme.current.styles.heading.copy(fontSize = 18.sp)
    )
}

@Composable
private fun largeLayout(
    horizontalPadding: Dp,
    verticalPadding: Dp
) {
    SelectionContainer {
        Column {
            Row(
                modifier = Modifier.padding(top = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(horizontalPadding)
            ) {
                Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                    Text(
                        text = stringResource(Res.string.about_business_heading),
                        style = LocalTheme.current.styles.heading
                    )
                    Text(
                        modifier = Modifier.padding(top = 32.dp),
                        text = buildAnnotatedLinkString(
                            stringResource(Res.string.about_business_summary),
                            onLinkClicked = { link ->
                                window.open(link)
                            }
                        ),
                        style = LocalTheme.current.styles.regular
                    )

                    GetInTouchText()
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

            Content(verticalPadding, isMobile = false)

            GetInTouchText(modifier = Modifier.align(Alignment.CenterHorizontally))

            Spacer(Modifier.height(verticalPadding))
        }
    }
}

@Composable
private fun Content(
    verticalPadding: Dp,
    isMobile: Boolean
) {
    Text(
        modifier = Modifier.padding(top = verticalPadding),
        text = stringResource(Res.string.about_business_list_heading),
        style = LocalTheme.current.styles.category.copy(
            color = LocalTheme.current.colors.primary,
            textDecoration = TextDecoration.Underline
        )
    )

    Column(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(verticalPadding)
    ) {
        listOf(
            Triple(Res.string.about_business_list_0, Res.string.about_business_list_0_content, Asset.Image.NoRobots),
            Triple(Res.string.about_business_list_1, Res.string.about_business_list_1_content, Asset.Image.SolutionIntegration),
            Triple(Res.string.about_business_list_2, Res.string.about_business_list_2_content, Asset.Image.CustomerSupport),
            Triple(Res.string.about_business_list_3, Res.string.about_business_list_3_content, Asset.Image.SentimentAnalysis),
            Triple(Res.string.about_business_list_4, Res.string.about_business_list_4_content, Asset.Image.ChatIntegration),
            Triple(Res.string.about_business_list_5, Res.string.about_business_list_5_content, Asset.Image.DataAssurance),
        ).forEachIndexed { index, data ->
            if(isMobile) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AsyncImageThumbnail(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .size(240.dp)
                            .clip(LocalTheme.current.shapes.componentShape),
                        asset = data.third,
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = stringResource(data.first),
                        style = LocalTheme.current.styles.subheading.copy(
                            textAlign = TextAlign.Center
                        )
                    )
                    Text(
                        modifier = Modifier.padding(top = 8.dp),
                        text = stringResource(data.second),
                        style = LocalTheme.current.styles.regular
                    )
                }
            }else {
                Row(modifier = Modifier.fillMaxWidth()) {
                    if(index % 2 == 0) {
                        AsyncImageThumbnail(
                            modifier = Modifier
                                .weight(.3f)
                                .clip(LocalTheme.current.shapes.componentShape),
                            asset = data.third,
                            contentScale = ContentScale.Crop
                        )
                    }

                    Column(
                        modifier = Modifier
                            .padding(top = 12.dp, start = 16.dp, end = 16.dp)
                            .weight(.7f)
                    ) {
                        Text(
                            text = stringResource(data.first),
                            style = LocalTheme.current.styles.subheading
                        )
                        Text(
                            modifier = Modifier
                                .padding(top = 8.dp, start = 12.dp)
                                .fillMaxWidth(.75f),
                            text = stringResource(data.second),
                            style = LocalTheme.current.styles.regular
                        )
                    }

                    if(index % 2 != 0) {
                        AsyncImageThumbnail(
                            modifier = Modifier
                                .weight(.3f)
                                .clip(LocalTheme.current.shapes.componentShape),
                            asset = data.third,
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}
