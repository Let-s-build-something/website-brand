package augmy.interactive.com.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import augmy.interactive.com.navigation.NavigationNode.Companion.allDestinations
import augmy.interactive.com.ui.ContactsScreen
import augmy.interactive.com.ui.DeleteMeScreen
import augmy.interactive.com.ui.RoadmapScreen
import augmy.interactive.com.ui.about.AboutBusinessScreen
import augmy.interactive.com.ui.about.AboutResearchScreen
import augmy.interactive.com.ui.about.AboutScreen
import augmy.interactive.com.ui.landing.LandingScreen

val DEFAULT_START_DESTINATION = NavigationNode.Landing.route

/** Host of the main navigation tree */
@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String? = null
) {
    println("kostka_test, destination: $startDestination, contains: ${allDestinations.contains(startDestination)}, allDestinations: $allDestinations")
    NavHost(
        modifier = modifier.fillMaxSize(),
        navController = navController,
        startDestination = startDestination.takeIf {
            allDestinations.contains(it)
        } ?: DEFAULT_START_DESTINATION
    ) {
        composable(NavigationNode.Landing.route) {
            LandingScreen()
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