package augmy.interactive.com.navigation

import androidx.navigation.NavDeepLink

sealed class NavigationNode {
    abstract val route: String

    protected open val deeplinkPath: String = route

    val deeplink
        get() = NavDeepLink("https://augmy.org$deeplinkPath")

    data object Landing : NavigationNode() {
        override val route = "/"
        override val deeplinkPath: String = "/"
    }

    data object BusinessAbout : NavigationNode() {
        override val route = "/business"
        override val deeplinkPath: String = "/business"
    }

    data object ResearchAbout : NavigationNode() {
        override val route = "/research"
        override val deeplinkPath: String = "/research"
    }

    data object PublicAbout : NavigationNode() {
        override val route = "/about"
        override val deeplinkPath: String = "/about"
    }

    data object Contacts : NavigationNode() {
        override val route = "/contacts"
        override val deeplinkPath: String = "/contacts"
    }

    data object Faq : NavigationNode() {
        override val route = "/faq"
        override val deeplinkPath: String = "/faq"
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
        override val deeplinkPath: String = "/roadmap"
    }

    data object DeleteMe : NavigationNode() {
        override val route = "/delete-me"
        override val deeplinkPath: String = "/delete-me"
    }
}
