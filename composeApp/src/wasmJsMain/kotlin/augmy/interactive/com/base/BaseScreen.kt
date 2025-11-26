package augmy.interactive.com.base

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import augmy.interactive.com.base.theme.isDarkTheme
import augmy.interactive.com.data.Asset
import augmy.interactive.com.shared.SharedViewModel
import augmy.interactive.com.theme.LocalTheme
import augmy.interactive.com.ui.components.AsyncSvgImage
import augmy.interactive.com.ui.components.HorizontalToolbar
import augmy.interactive.com.ui.components.ScrollBarProgressIndicator
import kotlinx.browser.window
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import website_brand.composeapp.generated.resources.Res
import website_brand.composeapp.generated.resources.contacts_bluesky
import website_brand.composeapp.generated.resources.contacts_bluesky_tag
import website_brand.composeapp.generated.resources.contacts_discord
import website_brand.composeapp.generated.resources.contacts_discord_tag
import website_brand.composeapp.generated.resources.contacts_instagram
import website_brand.composeapp.generated.resources.contacts_instagram_tag
import website_brand.composeapp.generated.resources.contacts_linkedin
import website_brand.composeapp.generated.resources.contacts_linkedin_tag
import website_brand.composeapp.generated.resources.contacts_twitter
import website_brand.composeapp.generated.resources.contacts_twitter_tag
import website_brand.composeapp.generated.resources.website_footer
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Most basic all-in-one implementation of a screen with action bar, without bottom bar
 * @param content screen content under the action bar
 */
@Composable
fun BaseScreen(
    modifier: Modifier = Modifier,
    viewModel: SharedViewModel,
    appBarVisible: Boolean = LocalHeyIamScreen.current.not(),
    contentColor: Color = Color.Transparent,
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    val focusManager = LocalFocusManager.current

    val previousSnackbarHostState = LocalSnackbarHost.current
    val snackbarHostState = remember {
        previousSnackbarHostState ?: SnackbarHostState()
    }

    CompositionLocalProvider(
        LocalSnackbarHost provides snackbarHostState,
        LocalHeyIamScreen provides true
    ) {
        Scaffold(
            modifier = modifier
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                },
            snackbarHost = {
                if(previousSnackbarHostState == null) {
                    BaseSnackbarHost(hostState = snackbarHostState)
                }
            },
            containerColor = Color.Transparent,
            contentColor = contentColor,
            floatingActionButton = floatingActionButton,
            floatingActionButtonPosition = floatingActionButtonPosition,
            content = { paddingValues ->
                val density = LocalDensity.current
                val contentSize = remember {
                    mutableStateOf(Size(0f, 0f))
                }

                Column(
                    modifier = modifier
                        .padding(paddingValues)
                        .onGloballyPositioned {
                            contentSize.value = with(density) {
                                Size(it.size.width.toDp().value, it.size.height.toDp().value)
                            }
                        },
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if(appBarVisible) {
                        HorizontalToolbar(viewModel = viewModel)
                    }
                    CompositionLocalProvider(LocalContentSizeDp provides contentSize.value) {
                        content(this)
                    }
                }
            }
        )
    }
}

/**
 * Default content for screen that should collapse based on the device width.
 * This custom limitation is due to some screens not needing the extra space and not having for example dual pane layout.
 */
@Composable
fun ModalScreenContent(
    modifier: Modifier = Modifier,
    scrollState: ScrollState? = rememberScrollState(),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(8.dp),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    footer: @Composable ColumnScope.() -> Unit = { FooterScreenContent() },
    content: @Composable ColumnScope.() -> Unit
) {
    Box {
        if(scrollState != null) {
            ScrollBarProgressIndicator(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .zIndex(2f),
                scrollState = scrollState
            )
        }

        Column(
            modifier = Modifier
                .animateContentSize()
                .fillMaxSize()
                .then(
                    if(scrollState != null) Modifier.verticalScroll(scrollState) else Modifier
                )
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = modifier
                    .widthIn(max = MaxModalWidthDp.dp)
                    .heightIn(min = LocalContentSizeDp.current.height.dp),
                verticalArrangement = verticalArrangement,
                horizontalAlignment = horizontalAlignment,
                content = content
            )
            footer()
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun FooterScreenContent(modifier: Modifier = Modifier) {
    val isCompact = LocalDeviceType.current == WindowWidthSizeClass.Compact

    val socialIcons = @Composable {
        Row(
            modifier = Modifier.padding(start = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SocialLogo(
                tag = Res.string.contacts_discord_tag,
                link = Res.string.contacts_discord,
                asset = Asset.Logo.Discord,
                tint = if(isDarkTheme) Color.White else Color.Black
            )
            SocialLogo(
                size = 28.dp,
                tag = Res.string.contacts_twitter_tag,
                link = Res.string.contacts_twitter,
                asset = Asset.Logo.Twitter,
                tint = if(isDarkTheme) Color.White else Color.Black
            )
            SocialLogo(
                tag = Res.string.contacts_bluesky_tag,
                link = Res.string.contacts_bluesky,
                asset = Asset.Logo.Bluesky
            )
            SocialLogo(
                tag = Res.string.contacts_instagram_tag,
                link = Res.string.contacts_instagram,
                asset = Asset.Logo.Instagram
            )
            SocialLogo(
                tag = Res.string.contacts_linkedin_tag,
                link = Res.string.contacts_linkedin,
                asset = Asset.Logo.LinkedIn
            )
        }
    }

    Row(
        modifier = modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
            .shadow(4.dp)
            .background(LocalTheme.current.colors.toolbarColor)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        SelectionContainer {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if(isCompact) socialIcons()
                Row(
                    modifier = Modifier
                        .widthIn(max = MaxModalWidthDp.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = if(isCompact) Arrangement.Center else Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(
                            Res.string.website_footer,
                            Clock.System.now().toLocalDateTime(
                                TimeZone.currentSystemDefault()
                            ).year
                        ),
                        style = LocalTheme.current.styles.category.copy(
                            textAlign = TextAlign.Center
                        )
                    )
                    if(!isCompact) socialIcons()
                }
            }
        }
    }
}

/** Logo with a tag of social media site */
@Composable
fun SocialLogo(
    modifier: Modifier = Modifier,
    tag: StringResource,
    link: StringResource,
    asset: Asset,
    size: Dp = 32.dp,
    tint: Color? = null
) {
    val url = stringResource(link)

    AsyncSvgImage(
        modifier = modifier
            .clickable {
                window.open(url)
            }
            .size(size),
        model = asset.url,
        tint = tint,
        contentDescription = stringResource(tag)
    )
}


/** Maximum width of a modal element - this can include a screen in case the device is a desktop */
const val MaxModalWidthDp = 1300

/** Callable onbackpressed function */
val LocalOnBackPress = staticCompositionLocalOf<(() -> Unit)?> { null }

/** size in DP of the available content for screens */
val LocalContentSizeDp = staticCompositionLocalOf { Size(0f, 0f) }

val LocalIsMouseUser = staticCompositionLocalOf { false }

/** Indication of whether the scope below is within a screen */
val LocalHeyIamScreen = staticCompositionLocalOf { false }

/** Current device frame type */
val LocalDeviceType = staticCompositionLocalOf { WindowWidthSizeClass.Medium }

/** current snackbar host for showing snackbars */
val LocalSnackbarHost = staticCompositionLocalOf<SnackbarHostState?> { null }

/** Default page size based on current device tye */
val LocalNavController = staticCompositionLocalOf<NavHostController?> { null }