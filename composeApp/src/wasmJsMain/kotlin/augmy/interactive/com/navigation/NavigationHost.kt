package augmy.interactive.com.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import augmy.interactive.com.ui.LandingScreen
import augmy.interactive.com.ui.about.AboutBusinessScreen
import augmy.interactive.com.ui.about.AboutResearchScreen
import augmy.interactive.com.ui.about.AboutScreen
import augmy.interactive.com.ui.about.ContactsScreen
import augmy.interactive.shared.ui.theme.LocalTheme

val DEFAULT_START_DESTINATION = NavigationNode.Landing.route

/** Host of the main navigation tree */
@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String? = null
) {
    NavHost(
        modifier = modifier
            .fillMaxSize()
            .background(LocalTheme.current.colors.backgroundLight),
        navController = navController,
        startDestination = startDestination ?: DEFAULT_START_DESTINATION
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
    }
}