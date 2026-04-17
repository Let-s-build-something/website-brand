package augmy.interactive.com.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavHostController
import androidx.navigation.NavUri
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import androidx.savedstate.read
import augmy.interactive.com.shared.SharedViewModel
import augmy.interactive.com.ui.ContactsScreen
import augmy.interactive.com.ui.DeleteMeScreen
import augmy.interactive.com.ui.LoginScreen
import augmy.interactive.com.ui.RoadmapScreen
import augmy.interactive.com.ui.about.AboutBusinessScreen
import augmy.interactive.com.ui.about.AboutResearchScreen
import augmy.interactive.com.ui.about.AboutScreen
import augmy.interactive.com.ui.faq.FaqScreen
import augmy.interactive.com.ui.landing.LandingScreen
import augmy.interactive.com.ui.users.UserDetailScreen

val DEFAULT_START_DESTINATION = NavigationNode.Landing.route

/** Host of the main navigation tree */
@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    model: SharedViewModel,
    startDestination: String? = null
) {
    LaunchedEffect(startDestination) {
        try {
            val uri = NavUri("https://augmy.org$startDestination")
            val request = NavDeepLinkRequest.Builder.fromUri(uri).build()
            val match = navController.graph.matchDeepLink(request)

            if (match != null) {
                navController.navigate(
                    uri,
                    navOptions {
                        launchSingleTop = true
                    }
                )
            }
        }catch (e: Exception) { e.printStackTrace() }
    }

    NavHost(
        modifier = modifier.fillMaxSize(),
        navController = navController,
        startDestination = DEFAULT_START_DESTINATION
    ) {
        composable(NavigationNode.Landing.route) {
            LandingScreen(model)
        }
        composable(NavigationNode.Faq.route) {
            FaqScreen()
        }
        composable(
            NavigationNode.Login.route,
            deepLinks = NavigationNode.Login.deeplink?.let { listOf(it) }.orEmpty(),
        ) { backStackEntry ->
            val nonce = remember {
                backStackEntry.arguments?.read { getString("nonce") }
            }
            val token = remember {
                backStackEntry.arguments?.read { getString("loginToken") }
            }

            LoginScreen(
                nonce = nonce,
                loginToken = token
            )
        }
        composable(NavigationNode.BusinessAbout.route) {
            AboutBusinessScreen()
        }
        composable(
            NavigationNode.UserDetail.route,
            deepLinks = NavigationNode.UserDetail.deeplink?.let { listOf(it) }.orEmpty(),
        ) { backStackEntry ->
            val userId = remember {
                backStackEntry.arguments?.read { getString("userId") }
            }

            UserDetailScreen(userId)
        }
        composable(NavigationNode.ResearchAbout.route) {
            AboutResearchScreen()
        }
        composable(NavigationNode.PublicAbout.route) {
            AboutScreen()
        }
        composable(NavigationNode.Contacts.route) {
            ContactsScreen()
        }
        composable(NavigationNode.Roadmap.route) {
            RoadmapScreen()
        }
        composable(NavigationNode.DeleteMe.route) {
            DeleteMeScreen()
        }
    }
}