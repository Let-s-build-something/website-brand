package augmy.interactive.com.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PresenceEventContent(
    val presence: Presence,
    val avatarUrl: String? = null,
    val displayName: String? = null,
    val lastActiveAgo: Long? = null,
    val isCurrentlyActive: Boolean? = null,
    val statusMessage: String? = null
)

enum class Presence(val value: String) {
    @SerialName("online")
    ONLINE("online"),

    @SerialName("offline")
    OFFLINE("offline"),

    @SerialName("unavailable")
    UNAVAILABLE("unavailable")
}
