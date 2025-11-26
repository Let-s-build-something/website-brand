package augmy.interactive.com

import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import augmy.interactive.com.base.BaseScreen
import augmy.interactive.com.base.BaseSnackbarHost
import augmy.interactive.com.base.LocalDeviceType
import augmy.interactive.com.base.LocalIsMouseUser
import augmy.interactive.com.base.LocalNavController
import augmy.interactive.com.base.LocalSnackbarHost
import augmy.interactive.com.base.theme.AugmyTheme
import augmy.interactive.com.navigation.NavigationHost
import augmy.interactive.com.shared.SharedViewModel
import augmy.interactive.com.shared.ThemeChoice
import augmy.interactive.com.theme.LocalTheme
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

    val mouseUser = rememberSaveable {
        mutableStateOf(false)
    }
    val hoverInteractionSource = if (!mouseUser.value) {
        remember { MutableInteractionSource() }
    }else null
    val isHovered = hoverInteractionSource?.collectIsHoveredAsState()

    LaunchedEffect(isHovered?.value) {
        if (isHovered?.value == true) {
            mouseUser.value = true
        }
    }

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
            modifier = Modifier
                .then(
                    if (hoverInteractionSource != null) {
                        Modifier.hoverable(
                            enabled = !mouseUser.value,
                            interactionSource = hoverInteractionSource
                        )
                    }else Modifier
                ),
            snackbarHost = {
                BaseSnackbarHost(hostState = snackbarHostState)
            },
            containerColor = LocalTheme.current.colors.backgroundLight,
            contentColor = LocalTheme.current.colors.backgroundLight
        ) { _ ->
            CompositionLocalProvider(
                LocalNavController provides navController,
                LocalSnackbarHost provides snackbarHostState,
                LocalDeviceType provides windowSizeClass.widthSizeClass,
                LocalIsMouseUser provides mouseUser.value
            ) {
                BaseScreen(
                    modifier = Modifier.fillMaxSize(),
                    viewModel = viewModel
                ) {
                    NavigationHost(
                        navController = navController,
                        startDestination = startDestination,
                        model = viewModel
                    )
                }
            }
        }
    }
}