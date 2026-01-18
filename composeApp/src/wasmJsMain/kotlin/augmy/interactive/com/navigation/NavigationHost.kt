package augmy.interactive.com.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.savedstate.read
import augmy.interactive.com.navigation.NavigationNode.Companion.allDestinations
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

val DEFAULT_START_DESTINATION = NavigationNode.Landing.route

/** Host of the main navigation tree */
@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    model: SharedViewModel,
    startDestination: String? = null
) {
    val finalStart = remember(startDestination) {
        val destinations = allDestinations
        println("kostka_test, start: $startDestination, destinations: $destinations")


        if (!startDestination.isNullOrBlank()) {
            val base = startDestination
                .takeWhile { it != '?' }
                .takeWhile { it != '&' }
                .ifBlank { DEFAULT_START_DESTINATION }
            val exists = destinations.any { it.contains(base) }
            if (exists) startDestination else DEFAULT_START_DESTINATION
        } else {
            DEFAULT_START_DESTINATION
        }
    }

    println("kostka_test, destination: $startDestination, contains: ${allDestinations.contains(startDestination)}, allDestinations: $allDestinations")
    NavHost(
        modifier = modifier.fillMaxSize(),
        navController = navController,
        startDestination = finalStart
    ) {
        composable(NavigationNode.Landing.route) {
            LandingScreen(model)
        }
        composable(NavigationNode.Faq.route) {
            FaqScreen()
        }
        composable(NavigationNode.Login.route) { backStackEntry ->
            val nonce = remember {
                backStackEntry.arguments?.read { getString("nonce") }
            }
            val token = remember {
                backStackEntry.arguments?.read { getString("loginToken") }
            }

            println("kostka_test, startDestination: $startDestination, nonce: $nonce, token: $token")

            LoginScreen(
                nonce = nonce,
                loginToken = token
            )
        }
        composable(NavigationNode.BusinessAbout.route) {
            AboutBusinessScreen()
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