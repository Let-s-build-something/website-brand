package augmy.interactive.com

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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


@OptIn(ExperimentalComposeUiApi::class, ExperimentalWasmJsInterop::class)
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
                val currentEntry by navController.currentBackStackEntryAsState()

                val initialUrl = remember {
                    window.location.pathname.removePrefix("/") + window.location.search
                }

                CompositionLocalProvider(LocalOnBackPress provides { window.history.go(-1) }) {
                    App(
                        navController = navController,
                        startDestination = initialUrl
                    )
                }

                LaunchedEffect(currentEntry) {
                    val destination = currentEntry?.destination?.route ?: return@LaunchedEffect

                    if (!destination.contains("{")) {
                        val browserPath = if (destination.startsWith("/")) destination else "/$destination"

                        if (window.location.pathname != browserPath) {
                            window.history.pushState(null, "", browserPath)
                        }
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