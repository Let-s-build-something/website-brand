package augmy.interactive.com.shared

/** Configuration specific to this application */
data class LocalSettings(

    /** user-selected theme for this application */
    val theme: ThemeChoice = ThemeChoice.SYSTEM
)

enum class ThemeChoice {
    LIGHT,
    DARK,
    SYSTEM
}