package augmy.interactive.com.base

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import augmy.interactive.com.ui.components.HorizontalAppBar
import augmy.interactive.shared.ui.base.BaseSnackbarHost

/**
 * Most basic all-in-one implementation of a screen with action bar, without bottom bar
 * @param navigationIcon what type of navigation icon screen should have
 * @param title capital title of the screen
 * @param subtitle lower case subtitle of the screen
 * @param actionIcons right side actions to be displayed
 * @param content screen content under the action bar
 */
@Composable
fun BaseScreen(
    modifier: Modifier = Modifier,
    navigationIcon: Pair<ImageVector, String>? = null,
    title: String? = null,
    subtitle: String? = null,
    onNavigationIconClick: () -> Unit = {},
    actionIcons: @Composable (Boolean) -> Unit = {},
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
                Column(
                    modifier = modifier.padding(paddingValues),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if(appBarVisible) {
                        HorizontalAppBar(
                            title = title,
                            navigationIcon = navigationIcon,
                            subtitle = subtitle,
                            actions = actionIcons,
                            onNavigationIconClick = onNavigationIconClick
                        )
                    }
                    content(this)
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
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(8.dp),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .widthIn(max = MaxModalWidthDp.dp)
            .fillMaxHeight()
            .padding(
                top = 24.dp,
                start = 16.dp,
                end = 16.dp
            ),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        content = content
    )
}

/** Current device screen size in DP (density pixels) */
val LocalScreenSize = staticCompositionLocalOf {
    IntSize(0, 0)
}

/** Maximum width of a modal element - this can include a screen in case the device is a desktop */
const val MaxModalWidthDp = 520

/** Callable onbackpressed function */
val LocalOnBackPress = staticCompositionLocalOf<(() -> Unit)?> { null }

/** Indication of whether the scope below is within a screen */
val LocalHeyIamScreen = staticCompositionLocalOf { false }

/** Current device frame type */
val LocalDeviceType = staticCompositionLocalOf { WindowWidthSizeClass.Medium }

/** current snackbar host for showing snackbars */
val LocalSnackbarHost = staticCompositionLocalOf<SnackbarHostState?> { null }

/** Default page size based on current device tye */
val LocalNavController = staticCompositionLocalOf<NavHostController?> { null }

/** Custom on back pressed provided by parent */
val LocalOnBackPressDispatcher = staticCompositionLocalOf<(() -> Unit)?> { null }