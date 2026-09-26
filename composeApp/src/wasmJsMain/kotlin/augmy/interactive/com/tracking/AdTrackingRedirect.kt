package augmy.interactive.com.tracking

import io.ktor.http.Url
import kotlinx.browser.window

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("() => navigator.userAgent")
private external fun browserUserAgent(): String

@OptIn(ExperimentalWasmJsInterop::class)
private fun detectStoreRedirect(): String? {
    val userAgent = browserUserAgent().lowercase()

    return when {
        "android" in userAgent -> "googleplay"

        "iphone" in userAgent ||
                "ipad" in userAgent ||
                "ipod" in userAgent -> "appstore"

        else -> null
    }
}
@OptIn(ExperimentalWasmJsInterop::class)
internal fun captureAdRedirect(): AdTrackingRequest? {
    val path = window.location.pathname
        .trimEnd('/')
        .ifBlank { "/" }

    if (path != "/r") {
        return null
    }

    val parameters =
        Url(window.location.href).parameters

    val request = AdTrackingRequest(
        source = parameters["source"],
        utmSource = parameters["utm_source"],
        utmMedium = parameters["utm_medium"],
        utmCampaign = parameters["utm_campaign"],
        utmContent = parameters["utm_content"],
        adId = parameters["ad_id"],

        redirect = parameters["redirect"]
            ?: detectStoreRedirect()
    )

    try {
        window.history.replaceState(
            null,
            "",
            "/"
        )
    } catch (exception: Exception) {
        exception.printStackTrace()
    }

    return request
}