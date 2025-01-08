package augmy.interactive.com.navigation


/** Main holder of all navigation nodes */
sealed class NavigationNode {

    /**
     * Deeplink is the appendix of a brand link, which is shared between all client apps.
     * Null if deeplink isn't supported
     */
    abstract val route: String

    /** home/landing screen of the whole app */
    data object Landing: NavigationNode() {
        override val route: String = "/"
    }

    /** Business screen for business-related audience */
    data object BusinessAbout: NavigationNode() {
        override val route: String = "/business"
    }

    /** About screen for scientific audience */
    data object ResearchAbout: NavigationNode() {
        override val route: String = "/research"
    }

    /** General about screen for the wider public */
    data object PublicAbout: NavigationNode() {
        override val route: String = "/about"
    }

    /** Screen for ways of contacting both the team, and the company */
    data object Contacts: NavigationNode() {
        override val route: String = "/contacts"
    }

    /** Screen for outlining the project's roadmap */
    data object Roadmap: NavigationNode() {
        override val route: String = "/roadmap"
    }

    /** Screen for users to remove themselves from the database */
    data object DeleteMe: NavigationNode() {
        override val route: String = "/delete-me"
    }

    companion object {
        val allDestinations = listOf(
            Landing.route,
            BusinessAbout.route,
            ResearchAbout.route,
            PublicAbout.route,
            Roadmap.route,
            DeleteMe.route,
            Contacts.route
        )
    }
}