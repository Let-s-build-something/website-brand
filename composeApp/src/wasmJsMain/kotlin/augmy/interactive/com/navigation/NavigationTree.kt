package augmy.interactive.com.navigation

import androidx.navigation.NavDeepLink

sealed class NavigationNode {
    abstract val route: String

    protected open val deeplinkPath: String? = null

    val deeplink
        get() = deeplinkPath?.let { NavDeepLink("https://augmy.org$it") }

    data object Landing : NavigationNode() {
        override val route = "/"
    }

    data object BusinessAbout : NavigationNode() {
        override val route = "/business"
    }

    data object ResearchAbout : NavigationNode() {
        override val route = "/research"
    }

    data object PublicAbout : NavigationNode() {
        override val route = "/about"
    }

    data object Contacts : NavigationNode() {
        override val route = "/contacts"
    }

    data object Faq : NavigationNode() {
        override val route = "/faq"
    }

    data object Login : NavigationNode() {
        override val route = "/login"
        override val deeplinkPath: String = "/login?nonce={nonce}&loginToken={loginToken}"
    }

    data object UserDetail : NavigationNode() {
        override val route = "/users/{userId}"
        override val deeplinkPath: String = "/users/{userId}"
    }

    data object Roadmap : NavigationNode() {
        override val route = "/roadmap"
    }

    data object DeleteMe : NavigationNode() {
        override val route = "/delete-me"
    }
}
