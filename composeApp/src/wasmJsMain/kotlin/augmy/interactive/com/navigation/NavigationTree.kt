package augmy.interactive.com.navigation

sealed class NavigationNode {
    abstract val route: String

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
        override val route = "/login?nonce={nonce}&loginToken={loginToken}"
    }

    data object Roadmap : NavigationNode() {
        override val route = "/roadmap"
    }

    data object DeleteMe : NavigationNode() {
        override val route = "/delete-me"
    }

    companion object {
        val allDestinations = listOf(
            Landing.route,
            Faq.route,
            Login.route,
            BusinessAbout.route,
            ResearchAbout.route,
            "/about",
            Roadmap.route,
            DeleteMe.route,
            Contacts.route
        )
    }
}
