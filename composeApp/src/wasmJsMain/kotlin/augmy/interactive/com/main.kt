package augmy.interactive.com

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.savedstate.read
import augmy.interactive.com.base.LocalOnBackPress
import augmy.interactive.com.injection.commonModule
import augmy.interactive.com.navigation.NavigationNode
import kotlinx.browser.document
import kotlinx.browser.window
import org.koin.core.context.startKoin

// paranoid check
private var isAppInitialized = false

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("(s) => encodeURIComponent(s)")
external fun encodeURIComponent(s: String): String

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
                    window.location.pathname
                        .trimEnd('/')
                        .split("/")
                        .joinToString("/") { if (it.startsWith("@") || it.contains(":")) encodeURIComponent(it) else it } +
                            window.location.search
                }


                CompositionLocalProvider(LocalOnBackPress provides { window.history.go(-1) }) {
                    App(
                        navController = navController,
                        startDestination = initialUrl
                    )
                }

                LaunchedEffect(currentEntry) {
                    val entry = currentEntry ?: return@LaunchedEffect
                    val route = entry.destination.route ?: return@LaunchedEffect

                    val browserPath = when {
                        route == NavigationNode.UserDetail.route || route.startsWith("/users") -> {
                            val userId = entry.arguments?.read { getString("userId") }
                            if (userId != null) "/users/${encodeURIComponent(userId)}" else "/users"
                        }
                        !route.contains("{") -> {
                            if (route.startsWith("/")) route else "/$route"
                        }
                        else -> return@LaunchedEffect
                    }

                    try {
                        if (window.location.pathname != browserPath) {
                            window.history.pushState(null, "", browserPath)
                        }
                    } catch (_: Exception) {}
                }

                window.onpopstate = {
                    try {
                        val path = window.location.pathname.trimEnd('/').ifBlank { "/" }
                        val success = navController.popBackStack(
                            route = path,
                            inclusive = false
                        )
                        if (!success) {
                            navController.navigate(path)
                        }
                    } catch (_: Exception) {}
                }
            }

        }catch (e: Exception) {
            e.printStackTrace()
        }
    }
}