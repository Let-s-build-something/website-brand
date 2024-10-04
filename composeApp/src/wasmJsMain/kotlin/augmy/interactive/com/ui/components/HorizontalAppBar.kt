package augmy.interactive.com.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.Switch
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import augmy.interactive.com.base.LocalDeviceType
import augmy.interactive.com.base.MaxModalWidthDp
import augmy.interactive.com.shared.SharedViewModel
import augmy.interactive.com.shared.ThemeChoice
import augmy.interactive.shared.ui.theme.LocalTheme
import org.jetbrains.compose.resources.stringResource
import website_brand.composeapp.generated.resources.Res
import website_brand.composeapp.generated.resources.accessibility_dark_mode
import website_brand.composeapp.generated.resources.accessibility_light_mode

/** Default, and minimum height of appbar */
const val AppBarHeightMobileDp = 48

/** Default, and minimum height of toolbar - desktop */
const val ToolBarHeightDesktopDpDesktop = 120

/**
 * Custom app bar with options of customization
 */
@Composable
fun HorizontalAppBar(
    modifier: Modifier = Modifier,
    viewModel: SharedViewModel
) {
    val localSettings = viewModel.localSettings.collectAsState()

    print(LocalDeviceType.current.toString())
    Box(
        modifier = modifier
            .shadow(elevation = 4.dp)
            .fillMaxWidth()
            .heightIn(min = if(LocalDeviceType.current == WindowWidthSizeClass.Expanded) {
                ToolBarHeightDesktopDpDesktop.dp
            }else AppBarHeightMobileDp.dp)
            .background(color = LocalTheme.current.colors.toolbarColor)
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .widthIn(max = MaxModalWidthDp.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.width(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Outlined.LightMode,
                    contentDescription = stringResource(Res.string.accessibility_light_mode),
                    tint = LocalTheme.current.colors.secondary
                )
                Switch(
                    modifier = Modifier.padding(horizontal = 6.dp),
                    checked = when(localSettings.value?.theme) {
                        ThemeChoice.DARK -> true
                        ThemeChoice.LIGHT -> false
                        else -> isSystemInDarkTheme()
                    },
                    colors = LocalTheme.current.styles.switchColorsDefault.copy(
                        checkedTrackColor = LocalTheme.current.colors.backgroundDark
                    ),
                    onCheckedChange = { isChecked ->
                        viewModel.updateTheme(isChecked)
                    }
                )
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Outlined.DarkMode,
                    contentDescription = stringResource(Res.string.accessibility_dark_mode),
                    tint = LocalTheme.current.colors.secondary
                )
            }
        }
    }
}