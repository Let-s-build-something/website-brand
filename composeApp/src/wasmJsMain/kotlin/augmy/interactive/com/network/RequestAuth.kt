package augmy.interactive.com.network

import augmy.interactive.com.BuildKonfig
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.URLProtocol
import io.ktor.util.AttributeKey

internal enum class RequestAuth {
    AdTracking
}

internal val RequestAuthKey =
    AttributeKey<RequestAuth>("RequestAuth")

internal fun HttpRequestBuilder.adTrackingAuth() {
    attributes.put(
        RequestAuthKey,
        RequestAuth.AdTracking
    )
    url {
        protocol = URLProtocol.HTTPS
        host = BuildKonfig.HttpsHostName
    }
}