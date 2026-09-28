package augmy.interactive.com.tracking

import augmy.interactive.com.BuildKonfig

internal class AdTrackingService(
    private val repository: AdTrackingRepository
) {

    suspend fun trackRedirect(
        request: AdTrackingRequest
    ): String? {
        val response =
            repository.track(request).getOrNull()
                ?: return null

        val redirectUrl =
            response.redirectUrl
                ?.takeIf { it.isNotBlank() }
                ?: return null

        return when (request.redirect) {
            "googleplay" -> addGoogleReferral(
                url = redirectUrl,
                clickId = response.clickId
            )

            "appstore" -> addAppleCampaign(
                url = redirectUrl,
                campaign = request.utmCampaign,
                providerToken = BuildKonfig.AppleProviderToken
            )

            else -> redirectUrl
        }
    }

    suspend fun trackStoreButton(
        redirectType: String,
        utmContent: String,
        referralUserId: String? = null
    ): String? {
        val request = AdTrackingRequest(
            source = "website",
            utmSource = "augmy.org",
            utmMedium = "website",
            utmCampaign = "store_buttons",
            utmContent = utmContent,
            redirect = redirectType
        )
        val response = repository.track(
            request
        ).getOrNull()
            ?: return null

        val redirectUrl =
            response.redirectUrl
                ?.takeIf { it.isNotBlank() }
                ?: return null

        return when (request.redirect) {
            "googleplay" -> addGoogleReferral(
                url = redirectUrl,
                clickId = response.clickId,
                referralUserId = referralUserId
            )

            "appstore" -> addAppleCampaign(
                url = redirectUrl,
                campaign = request.utmCampaign,
                providerToken = BuildKonfig.AppleProviderToken
            )

            else -> redirectUrl
        }
    }

    private fun addGoogleReferral(
        url: String,
        clickId: String? = null,
        referralUserId: String? = null
    ): String {
        val builder = io.ktor.http.URLBuilder(url)

        val referrerParts = mutableListOf<String>()

        val existingReferrer =
            builder.parameters["referrer"]

        if (!existingReferrer.isNullOrBlank()) {
            referrerParts += existingReferrer
        }

        if (!clickId.isNullOrBlank()) {
            referrerParts += "click_id=$clickId"
        }

        if (!referralUserId.isNullOrBlank()) {
            referrerParts += "ref=$referralUserId"
        }

        if (referrerParts.isNotEmpty()) {
            builder.parameters["referrer"] =
                referrerParts.joinToString("&")
        }

        return builder.buildString()
    }

    private fun addAppleCampaign(
        url: String,
        campaign: String?,
        providerToken: String?
    ): String {
        val builder = io.ktor.http.URLBuilder(url)

        if (
            !providerToken.isNullOrBlank() &&
            !campaign.isNullOrBlank()
        ) {
            builder.parameters["pt"] = providerToken
            builder.parameters["ct"] = campaign
            builder.parameters["mt"] = "8"
        }

        return builder.buildString()
    }
}