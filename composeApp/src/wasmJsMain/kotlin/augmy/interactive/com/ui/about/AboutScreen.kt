package augmy.interactive.com.ui.about

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import augmy.interactive.com.base.LocalContentSizeDp
import augmy.interactive.com.base.LocalDeviceType
import augmy.interactive.com.base.ModalScreenContent
import augmy.interactive.com.base.theme.draggable
import augmy.interactive.com.base.theme.scalingClickable
import augmy.interactive.com.data.Asset
import augmy.interactive.com.data.MediaIO
import augmy.interactive.com.data.NetworkItemIO
import augmy.interactive.com.theme.LocalTheme
import augmy.interactive.com.ui.components.AsyncSvgImage
import augmy.interactive.com.ui.components.AvatarImage
import augmy.interactive.com.ui.components.buildAnnotatedLink
import augmy.interactive.com.ui.components.buildAnnotatedLinkString
import augmy.interactive.com.ui.landing.components.isNanOrInfinite
import kotlinx.browser.window
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import website_brand.composeapp.generated.resources.Res
import website_brand.composeapp.generated.resources.about_content_0
import website_brand.composeapp.generated.resources.about_content_1
import website_brand.composeapp.generated.resources.about_future_content
import website_brand.composeapp.generated.resources.about_quote_honza
import website_brand.composeapp.generated.resources.about_quote_jacob
import website_brand.composeapp.generated.resources.about_quote_marek
import website_brand.composeapp.generated.resources.about_quote_rafail
import website_brand.composeapp.generated.resources.about_title_honza
import website_brand.composeapp.generated.resources.about_title_jacob
import website_brand.composeapp.generated.resources.about_title_marek
import website_brand.composeapp.generated.resources.about_title_rafail
import website_brand.composeapp.generated.resources.accessibility_signatures
import website_brand.composeapp.generated.resources.logo_monochrome
import website_brand.composeapp.generated.resources.signatures
import website_brand.composeapp.generated.resources.toolbar_action_about

/** Screen with general information about the project for wide public */
@Composable
fun AboutScreen() {
    val verticalPadding = (LocalContentSizeDp.current.height / 8)
        .takeIf { !it.isNanOrInfinite() }
        ?.dp
        ?: 32.dp

    ModalScreenContent(scrollState = rememberScrollState()) {
        Crossfade(LocalDeviceType.current == WindowWidthSizeClass.Compact) { isCompact ->
            if(isCompact) {
                compactLayout(verticalPadding)
            }else {
                largeLayout(verticalPadding)
            }
        }
    }
}

@Composable
private fun compactLayout(verticalPadding: Dp) {
    val density = LocalDensity.current
    val contentWidthDp = remember { mutableFloatStateOf(0f) }

    Column(
        modifier = Modifier
            .onSizeChanged {
                with(density) {
                    contentWidthDp.value = it.width.toDp().value
                }
            }
            .padding(start = 12.dp, end = 12.dp)
    ) {
        TextBlock(
            modifier = Modifier.fillMaxWidth(),
            verticalPadding = verticalPadding
        )

        Spacer(Modifier.height(verticalPadding))

        Column(
            modifier = Modifier.background(
                color = LocalTheme.current.colors.backgroundDark,
                shape = LocalTheme.current.shapes.componentShape
            )
        ) {
            val sharedModifier = Modifier
                .padding(horizontal = 8.dp, vertical = 12.dp)
                .fillMaxWidth()

            PersonBlock(
                modifier = sharedModifier,
                data = NetworkItemIO(
                    userId = "@jacob:augmy.org",
                    displayName = "Jakub Kostka",
                    avatar = MediaIO(
                        url = "https://augmy.org/storage/company/kostka_jakub_rectangle.jpg",
                        thumbnail = "https://augmy.org/storage/company/thumbnails/tn_kostka_jakub_rectangle.jpg"
                    )
                ),
                linkedIn = "https://www.linkedin.com/in/jakub-kostka-augmy",
                email = "jakub.kostka@augmy.org",
                title = stringResource(Res.string.about_title_jacob),
                quote = stringResource(Res.string.about_quote_jacob)
            )
            PersonBlock(
                modifier = sharedModifier,
                data = NetworkItemIO(
                    userId = "@marek_lan:augmy.org",
                    displayName = "Marek Langer",
                    avatar = MediaIO(
                        url = "https://augmy.org/storage/company/langer_marek_rectangle.jpg",
                        thumbnail = "https://augmy.org/storage/company/thumbnails/tn_langer_marek_rectangle.jpg"
                    )
                ),
                linkedIn = "https://www.linkedin.com/in/marek-langer-6215221b4/",
                email = "marek.langer@augmy.org",
                title = stringResource(Res.string.about_title_marek),
                quote = stringResource(Res.string.about_quote_marek)
            )
            PersonBlock(
                modifier = sharedModifier,
                data = NetworkItemIO(
                    userId = "@rafail_daskos:augmy.org",
                    displayName = "Rafail Daskos",
                    avatar = MediaIO(
                        url = "https://augmy.org/storage/company/daskos_rafail.jpg",
                        thumbnail = "https://augmy.org/storage/company/thumbnails/tn_daskos_rafail.jpg"
                    )
                ),
                linkedIn = "https://www.linkedin.com/in/rafail-daskos-3a3229151/",
                email = "rafail.daskos@augmy.org",
                title = stringResource(Res.string.about_title_rafail),
                quote = stringResource(Res.string.about_quote_rafail),
            )
            PersonBlock(
                modifier = sharedModifier,
                data = NetworkItemIO(
                    userId = "@johnny:augmy.org",
                    displayName = "Jan Jelínek",
                    avatar = MediaIO(
                        url = "https://augmy.org/storage/company/jelinek_jan.jpg",
                        thumbnail = "https://augmy.org/storage/company/thumbnails/tn_jelinek_jan.jpg"
                    )
                ),
                linkedIn = "https://www.linkedin.com/in/jan-jel%C3%ADnek-lead/",
                email = "jan.jelinek@augmy.org",
                title = stringResource(Res.string.about_title_honza),
                quote = stringResource(Res.string.about_quote_honza)
            )
        }

        Spacer(Modifier.height(verticalPadding))
    }
}

@Composable
private fun largeLayout(verticalPadding: Dp) {
    val density = LocalDensity.current
    val membersScrollState = rememberScrollState()
    val contentWidthDp = remember { mutableFloatStateOf(0f) }
    val tileHeightDp = remember { mutableFloatStateOf(0f) }

    Column(
        modifier = Modifier
            .onSizeChanged {
                with(density) {
                    contentWidthDp.value = it.width.toDp().value
                }
            }
            .padding(start = 12.dp, end = 12.dp),
        verticalArrangement = Arrangement.spacedBy(verticalPadding / 2)
    ) {
        TextBlock(
            modifier = Modifier.fillMaxWidth(),
            verticalPadding = verticalPadding
        )
        Row(
            modifier = Modifier
                .background(
                    color = LocalTheme.current.colors.backgroundDark,
                    shape = LocalTheme.current.shapes.componentShape
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .align(Alignment.CenterHorizontally)
                .horizontalScroll(membersScrollState)
                .draggable(state = membersScrollState, orientation = Orientation.Horizontal),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val sharedModifier = Modifier
                .width(contentWidthDp.value.div(4).dp - 24.dp)
                .animateContentSize()
                .heightIn(min = tileHeightDp.value.dp)
                .onSizeChanged {
                    with(density) {
                        if (it.height > tileHeightDp.value) {
                            tileHeightDp.value = it.width.toDp().value
                        }
                    }
                }

            PersonBlock(
                modifier = sharedModifier,
                data = NetworkItemIO(
                    userId = "@jacob:augmy.org",
                    displayName = "Jakub Kostka",
                    avatar = MediaIO(
                        url = "https://augmy.org/storage/company/kostka_jakub_rectangle.jpg",
                        thumbnail = "https://augmy.org/storage/company/thumbnails/tn_kostka_jakub_rectangle.jpg"
                    )
                ),
                linkedIn = "https://www.linkedin.com/in/jakub-kostka-augmy",
                email = "jakub.kostka@augmy.org",
                title = stringResource(Res.string.about_title_jacob),
                quote = stringResource(Res.string.about_quote_jacob)
            )
            PersonBlock(
                modifier = sharedModifier,
                data = NetworkItemIO(
                    userId = "@marek_lan:augmy.org",
                    displayName = "Marek Langer",
                    avatar = MediaIO(
                        url = "https://augmy.org/storage/company/langer_marek_rectangle.jpg",
                        thumbnail = "https://augmy.org/storage/company/thumbnails/tn_langer_marek_rectangle.jpg"
                    )
                ),
                linkedIn = "https://www.linkedin.com/in/marek-langer-6215221b4/",
                email = "marek.langer@augmy.org",
                title = stringResource(Res.string.about_title_marek),
                quote = stringResource(Res.string.about_quote_marek)
            )
            PersonBlock(
                modifier = sharedModifier,
                data = NetworkItemIO(
                    userId = "@rafail_daskos:augmy.org",
                    displayName = "Rafail Daskos",
                    avatar = MediaIO(
                        url = "https://augmy.org/storage/company/daskos_rafail.jpg",
                        thumbnail = "https://augmy.org/storage/company/thumbnails/tn_daskos_rafail.jpg"
                    )
                ),
                linkedIn = "https://www.linkedin.com/in/rafail-daskos-3a3229151/",
                email = "rafail.daskos@augmy.org",
                title = stringResource(Res.string.about_title_rafail),
                quote = stringResource(Res.string.about_quote_rafail),
            )
            PersonBlock(
                modifier = sharedModifier,
                data = NetworkItemIO(
                    userId = "@johnny:augmy.org",
                    displayName = "Jan Jelínek",
                    avatar = MediaIO(
                        url = "https://augmy.org/storage/company/jelinek_jan.jpg",
                        thumbnail = "https://augmy.org/storage/company/thumbnails/tn_jelinek_jan.jpg"
                    )
                ),
                linkedIn = "https://www.linkedin.com/in/jan-jel%C3%ADnek-lead/",
                email = "jan.jelinek@augmy.org",
                title = stringResource(Res.string.about_title_honza),
                quote = stringResource(Res.string.about_quote_honza)
            )
        }
        Spacer(Modifier.height(verticalPadding))
    }
}

@Composable
private fun PersonBlock(
    modifier: Modifier = Modifier,
    data: NetworkItemIO,
    email: String,
    linkedIn: String,
    title: String,
    quote: String
) {
    val isHovered = remember(data.userId) { mutableStateOf(false) }
    val corners = animateDpAsState(if (isHovered.value) 10.dp else 32.dp)

    SelectionContainer {
        Column(
            modifier = modifier
                .scalingClickable(
                    scaleInto = 1f,
                    onHover = { hovered -> isHovered.value = hovered }
                )
                .padding(vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            AvatarImage(
                modifier = Modifier
                    .fillMaxWidth(.6f)
                    .aspectRatio(1f),
                shape = RoundedCornerShape(corners.value),
                media = data.avatar,
                name = data.displayName,
                tag = null
            )
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = data.displayName ?: "",
                style = LocalTheme.current.styles.title.copy(
                    textAlign = TextAlign.Center
                )
            )
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = title,
                style = LocalTheme.current.styles.regular.copy(
                    textAlign = TextAlign.Center
                )
            )
            Row(
                modifier = Modifier.padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Outlined.AlternateEmail,
                    tint = LocalTheme.current.colors.disabled,
                    contentDescription = null
                )
                Text(
                    text = buildAnnotatedLinkString(email),
                    style = LocalTheme.current.styles.regular
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(Res.drawable.logo_monochrome),
                    tint = LocalTheme.current.colors.disabled,
                    contentDescription = null
                )
                data.userId?.let { userId ->
                    Text(
                        text = buildAnnotatedLink(
                            text = userId,
                            linkTexts = listOf(userId),
                            onLinkClicked = { link, _ ->
                                window.open("https://augmy.org/users/$userId")
                            }
                        ),
                        style = LocalTheme.current.styles.regular.copy(
                            color = LocalTheme.current.colors.disabled
                        )
                    )
                }
            }
            AsyncSvgImage(
                modifier = Modifier
                    .scalingClickable(scaleInto = .95f) {
                        window.open(linkedIn)
                    }
                    .size(32.dp),
                model = Asset.Logo.LinkedIn.url,
                contentDescription = "LinkedIn"
            )

            AnimatedVisibility(
                visible = isHovered.value || LocalDeviceType.current == WindowWidthSizeClass.Compact
            ) {
                if (quote.isNotBlank()) {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(top = 16.dp)
                            .background(
                                color = LocalTheme.current.colors.backgroundLight,
                                shape = LocalTheme.current.shapes.rectangularActionShape
                            )
                            .padding(vertical = 4.dp, horizontal = 6.dp),
                        text = quote,
                        style = LocalTheme.current.styles.regular.copy(
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun TextBlock(
    modifier: Modifier = Modifier,
    verticalPadding: Dp
) {
    val isCompact = LocalDeviceType.current == WindowWidthSizeClass.Compact

    SelectionContainer {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(top = verticalPadding / 3),
                text = stringResource(Res.string.toolbar_action_about),
                style = LocalTheme.current.styles.heading
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth(if (isCompact) 1f else .5f)
                    .padding(top = verticalPadding / 2),
                text = stringResource(Res.string.about_content_0),
                style = LocalTheme.current.styles.regular
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(if (isCompact) 1f else .5f)
                    .padding(top = 16.dp),
                text = stringResource(Res.string.about_content_1),
                style = LocalTheme.current.styles.regular
            )
            Image(
                modifier = Modifier.padding(top = 16.dp),
                painter = painterResource(Res.drawable.signatures),
                colorFilter = ColorFilter.tint(color = LocalTheme.current.colors.secondary),
                contentDescription = stringResource(Res.string.accessibility_signatures)
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(if (isCompact) 1f else .5f)
                    .padding(top = verticalPadding / 2),
                text = stringResource(Res.string.about_future_content),
                style = LocalTheme.current.styles.regular.copy(
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}
