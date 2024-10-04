package augmy.interactive.com

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import augmy.interactive.com.base.BaseScreen
import augmy.interactive.com.base.LocalDeviceType
import augmy.interactive.com.base.LocalNavController
import augmy.interactive.com.base.LocalSnackbarHost
import augmy.interactive.com.base.ModalScreenContent
import augmy.interactive.com.base.theme.AugmyTheme
import augmy.interactive.com.navigation.NavigationHost
import augmy.interactive.com.shared.SharedViewModel
import augmy.interactive.com.shared.ThemeChoice
import augmy.interactive.shared.ui.base.BaseSnackbarHost
import augmy.interactive.shared.ui.theme.LocalTheme
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun App(
    viewModel: SharedViewModel = koinViewModel(),
    navController: NavHostController,
    startDestination: String? = null
) {
    val localSettings = viewModel.localSettings.collectAsState()
    val windowSizeClass = calculateWindowSizeClass()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.initApp()
    }

    AugmyTheme(
        isDarkTheme = when(localSettings.value?.theme) {
            ThemeChoice.DARK -> true
            ThemeChoice.LIGHT -> false
            else -> isSystemInDarkTheme()
        }
    ) {
        Scaffold(
            snackbarHost = {
                BaseSnackbarHost(hostState = snackbarHostState)
            },
            containerColor = LocalTheme.current.colors.backgroundLight,
            contentColor = LocalTheme.current.colors.backgroundLight
        ) { _ ->
            BaseScreen {
                ModalScreenContent {
                    CompositionLocalProvider(
                        LocalNavController provides navController,
                        LocalSnackbarHost provides snackbarHostState,
                        LocalDeviceType provides windowSizeClass.widthSizeClass
                    ) {
                        NavigationHost(
                            navController = navController,
                            startDestination = startDestination
                        )
                    }
                }
            }
        }
    }
}