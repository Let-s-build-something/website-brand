package augmy.interactive.com.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/** user object of another use specific to our database */
@Serializable
data class NetworkItemIO @OptIn(ExperimentalUuidApi::class) constructor(

    val id: String = Uuid.random().toString(),
    val roomId: String? = null,
    @SerialName("user_id")
    val userId: String? = null,

    /** username of the current user */
    val displayName: String? = null,

    @SerialName("avatar_url")
    val avatarUrl: String? = null,

    val avatar: MediaIO? = MediaIO(url = avatarUrl),

    /** Whether the user is a mutually included */
    val isMutual: Boolean? = null,

    /** Last message sent within this network item */
    @Deprecated("Only rooms can have messages")
    val lastMessage: String? = null,

    /** Whether the user's configuration is public */
    val isPublic: Boolean? = null,

    /**
     * A decimal range between -1 and 10. -1 means blocked, 1 is muted,
     *  or just a far social circle, and 10 is the closest
     */
    val proximity: Float? = null,

    /** Override color for this network item */
    val color: String? = null,

    var ownerUserId: String? = null,

    val presence: PresenceEventContent? = null,
) {

    val tag: String?
        get() = "673b1e"
}
