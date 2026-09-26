package augmy.interactive.com.tracking

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AdTrackingRequest(
    val source: String? = null,

    @SerialName("utm_source")
    val utmSource: String? = null,

    @SerialName("utm_medium")
    val utmMedium: String? = null,

    @SerialName("utm_campaign")
    val utmCampaign: String? = null,

    @SerialName("utm_content")
    val utmContent: String? = null,

    @SerialName("ad_id")
    val adId: String? = null,

    val redirect: String? = null
)

@Serializable
internal data class AdTrackingResponse(
    @SerialName("click_id")
    val clickId: String,

    @SerialName("redirect_url")
    val redirectUrl: String? = null
)