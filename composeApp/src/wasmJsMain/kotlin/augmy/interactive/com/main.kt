package augmy.interactive.com

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import augmy.interactive.com.base.LocalOnBackPress
import augmy.interactive.com.injection.commonModule
import augmy.interactive.com.navigation.DEFAULT_START_DESTINATION
import kotlinx.browser.document
import kotlinx.browser.window
import org.koin.core.context.startKoin

// paranoid check
private var isAppInitialized = false

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    if(isAppInitialized.not()) {
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


                LaunchedEffect(currentEntry.value) {
                    if(currentEntry.value != null
                        && window.location.pathname != currentRoute
                    ) {
                        window.location.pathname = currentRoute
                    }
                }

                //https://developer.mozilla.org/en-US/docs/Web/API/Location
                LaunchedEffect(window.location.pathname) {
                    // navigate to correct route only if arguments are required, otherwise startDestination is sufficient
                    if(window.location.pathname != DEFAULT_START_DESTINATION
                        && currentEntry.value?.arguments?.isEmpty() == false
                    ) {
                        navController.navigate(route = window.location.pathname)
                    }
                }
            }
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }
}