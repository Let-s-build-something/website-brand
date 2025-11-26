package augmy.interactive.com.data

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi

@Serializable
data class MediaIO @OptIn(ExperimentalUuidApi::class) constructor(
    /** Access url for the media. Can be encrypted. */
    val url: String? = null,
    val thumbnail: String? = null,

    /** Type of media. Only generally reliable. */
    val mimetype: String? = null,

    /** The original file name */
    val name: String? = null,

    /** Size in bytes of the media */
    val size: Long? = null,

    val messageId: String? = null,

    val conversationId: String? = null,

    /** Local file path */
    val path: String? = null,

    val id: String = "${url}_${messageId}",
) {
    val isEmpty: Boolean
        get() = url.isNullOrBlank() && path.isNullOrBlank()

    var data: ByteArray? = null

    override fun toString(): String {
        return "{" +
                "url: $url, " +
                "mimetype: $mimetype, " +
                "messageId: $messageId, " +
                "conversationId: $conversationId, " +
                "id: $id, " +
                "name: $name, " +
                "size: $size, " +
                "path: $path" +
                "}"
    }
}
