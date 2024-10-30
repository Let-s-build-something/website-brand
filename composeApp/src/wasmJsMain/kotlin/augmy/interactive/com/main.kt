package augmy.interactive.com

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import augmy.interactive.com.base.LocalOnBackPress
import augmy.interactive.com.injection.commonModule
import kotlinx.browser.document
import kotlinx.browser.window
import org.koin.core.context.startKoin

// paranoid check
private var isAppInitialized = false


@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    if(isAppInitialized.not()) {
        document.getElementById("loader-container")?.remove()

        startKoin {
            modules(commonModule)
        }
        isAppInitialized = true
    }

    document.body?.let { body ->
        try {
            ComposeViewport(body) {
                val navController = rememberNavController()
                val currentEntry = navController.currentBackStackEntryAsState()
                val currentRoute = currentEntry.value?.destination?.route ?: window.location.pathname

                CompositionLocalProvider(
                    LocalOnBackPress provides {
                        window.history.go(-1)
                    }
                ) {
                    App(
                        navController = navController,
                        startDestination = window.location.pathname
                    )
                }

                LaunchedEffect(currentRoute) {
                    if(window.location.pathname != currentRoute) {
                        window.history.pushState(null, "", currentRoute)
                    }
                }

                window.onpopstate = {
                    // if pop back fails, user goes forward
                    if(!navController.popBackStack(
                            route = window.location.pathname,
                            inclusive = false
                        )) {
                        navController.navigate(window.location.pathname)
                    }
                }
            }
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }
}