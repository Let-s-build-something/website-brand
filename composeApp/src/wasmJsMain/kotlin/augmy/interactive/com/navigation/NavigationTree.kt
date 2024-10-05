package augmy.interactive.com.navigation

import kotlinx.serialization.Serializable

/** Main holder of all navigation nodes */
sealed class NavigationNode {

    /**
     * Deeplink is the appendix of a brand link, which is shared between all client apps.
     * Null if deeplink isn't supported
     */
    abstract val route: String

    /** home/landing screen of the whole app */
    @Serializable
    data object Landing: NavigationNode() {
        override val route: String = "/"
    }

    /** Business screen for business-related audience */
    @Serializable
    data object BusinessAbout: NavigationNode() {
        override val route: String = "/business"
    }

    /** About screen for scientific audience */
    @Serializable
    data object ResearchAbout: NavigationNode() {
        override val route: String = "/research"
    }

    /** General about screen for the wider public */
    @Serializable
    data object PublicAbout: NavigationNode() {
        override val route: String = "/about"
    }

    /** Screen for ways of contacting both the team, and the company */
    @Serializable
    data object Contacts: NavigationNode() {
        override val route: String = "/contacts"
    }


    companion object {
        val allDestinations = listOf(
            Landing.route,
            BusinessAbout.route,
            ResearchAbout.route,
            PublicAbout.route,
            Contacts.route
        )
    }
}