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
import kotlinx.coroutines.delay
import org.koin.core.context.startKoin

// paranoid check
private var isAppInitialized = false

// Define an interface for your global state
external interface GlobalAppState {
    var kotlinApp: Boolean?
}

fun polyfillRandomUUID() {
    js("""
        if (typeof crypto !== 'undefined' && typeof crypto.randomUUID !== 'function') {
            crypto.randomUUID = function() {
                return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
                    const r = Math.random() * 16 | 0;
                    const v = c === 'x' ? r : (r & 0x3 | 0x8);
                    return v.toString(16);
                });
            };
        }
    """)
}


@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    polyfillRandomUUID()

    if(isAppInitialized.not()) {
        (window as? GlobalAppState)?.kotlinApp = true

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
                //val currentRoute = currentEntry.value?.destination?.route ?: window.location.pathname

                CompositionLocalProvider(
                    LocalOnBackPress provides {
                        window.history.go(-1)
                    }
                ) {
                    App(
                        navController = navController,
                        //startDestination = window.location.pathname
                    )
                }


                /*LaunchedEffect(currentEntry.value) {
                    if(currentEntry.value != null
                        && window.location.pathname != currentRoute
                    ) {
                        window.location.pathname = currentRoute
                    }
                }*/

                //https://developer.mozilla.org/en-US/docs/Web/API/Location
                LaunchedEffect(window.location.pathname) {
                    delay(500)
                    // navigate to correct route only if arguments are required, otherwise startDestination is sufficient
                    if(window.location.pathname != DEFAULT_START_DESTINATION) {
                        navController.navigate(route = window.location.pathname)
                    }
                }
            }
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }
}