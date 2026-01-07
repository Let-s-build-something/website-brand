package augmy.interactive.com.ui.landing.components.avatar

import augmy.interactive.com.base.theme.Colors
import augmy.interactive.com.theme.SharedColors

data class AvatarConfiguration(
    val color: AvatarColorConfiguration = AvatarColorConfiguration(
        primary = Colors.ProximitySocial,
        secondary = Colors.HunterGreen,
        tertial = SharedColors.YELLOW
    ),
    val flower: AvatarFlowerConfiguration? = null,
    val floor: AvatarFloorConfiguration? = null,
    val head: AvatarHeadConfiguration? = null,
) {
    override fun toString(): String {
        return "{" +
                "color: ${color.primary}, ${color.secondary}, ${color.tertial}" +
                "flower: ${flower?.let { it::class.simpleName }}" +
                "floor: ${floor?.let { it::class.simpleName }}" +
                "head: ${head?.let { it::class.simpleName }}" +
                "}"
    }
}
