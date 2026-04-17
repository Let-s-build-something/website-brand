package augmy.interactive.com.shared

import augmy.interactive.com.shared.BaseResponse.Companion.getResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed class BaseResponse<out T> {

    data object Idle: BaseResponse<Nothing>()

    data object Loading: BaseResponse<Nothing>()

    data class Success<out T>(override val data: T) : BaseResponse<T>()

    @Serializable
    data class Error(
        /** list of error objects */
        val errors: List<String> = listOf(),

        /** User friendly message */
        @SerialName("error")
        val message: String? = null,

        /** The request block in milliseconds */
        val retryAfterMs: Int? = null,

        /** BE error code */
        @SerialName("errcode")
        val code: String? = null,

        @SerialName("soft_logout")
        val softLogout: Boolean = true
    ): BaseResponse<Nothing>() {
        var httpCode: Int = -1

        override fun toString() = "{" +
                "errors: $errors, " +
                "message: $message, " +
                "retryAfterMs: $retryAfterMs" +
                "code: $code" +
                "httpCode: $httpCode" +
                "}"
    }

    /** returns this object as a success */
    val success: Success<T>?
        get() = this as? Success<T>

    open val data: T?
        get() = (this as? Success<T>)?.data

    /** returns this object as an error */
    val error: Error?
        get() = this as? Error

    val isLoading: Boolean
        get() = this is Loading

    val isSuccess: Boolean
        get() = this is Success

    val isError: Boolean
        get() = this is Error

    companion object {
        suspend inline fun <reified T> HttpResponse.getResponse(): BaseResponse<T> {
            return when (status) {
                HttpStatusCode.OK,
                HttpStatusCode.Created,
                HttpStatusCode.Accepted,
                HttpStatusCode.NonAuthoritativeInformation -> Success(this.body<T>())
                else -> try {
                    this.body<Error>().apply { httpCode = status.value }
                }catch (_: Exception) {
                    Error().apply { httpCode = status.value }
                }
            }
        }

        inline fun <reified T> Result<T>.toResponse(): BaseResponse<T> {
            return try {
                Success(getOrThrow())
            }catch (e: Exception) {
                Error(message = e.message)
            }
        }
    }
}

suspend inline fun <reified T> HttpClient.safeRequest(
    block: HttpClient.() -> HttpResponse
): BaseResponse<T> = try {
    block().getResponse<T>()
} catch (e: UnresolvedAddressException) {
    e.printStackTrace()
    BaseResponse.Error()
}catch (e: Exception) {
    e.printStackTrace()
    BaseResponse.Error()
}
