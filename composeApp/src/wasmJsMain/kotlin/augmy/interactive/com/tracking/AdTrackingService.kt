package augmy.interactive.com.tracking

internal class AdTrackingService(
    private val repository: AdTrackingRepository
) {

    suspend fun trackRedirect(
        request: AdTrackingRequest
    ): String? {
        return repository
            .track(request)
            .getOrNull()
            ?.redirectUrl
            ?.takeIf { it.isNotBlank() }
    }

    suspend fun trackStoreButton(
        redirectType: String,
        utmContent: String,
        referralUserId: String? = null
    ): String? {
        val response = repository.track(
            AdTrackingRequest(
                source = "website",
                utmSource = "augmy.org",
                utmMedium = "website",
                utmCampaign = "store_buttons",
                utmContent = utmContent,
                adId = null,
                redirect = redirectType
            )
        ).getOrNull()

        val redirectUrl = response
            ?.redirectUrl
            ?.takeIf { it.isNotBlank() }
            ?: return null

        return if (
            redirectType == "googleplay" &&
            !referralUserId.isNullOrBlank()
        ) {
            addGoogleReferral(
                url = redirectUrl,
                referralUserId = referralUserId
            )
        } else {
            redirectUrl
        }
    }

    private fun addGoogleReferral(
        url: String,
        referralUserId: String
    ): String {
        val builder = io.ktor.http.URLBuilder(url)

        val existingReferrer =
            builder.parameters["referrer"]

        val referral =
            "ref=$referralUserId"

        builder.parameters["referrer"] =
            if (existingReferrer.isNullOrBlank()) {
                referral
            } else {
                "$existingReferrer&$referral"
            }

        return builder.buildString()
    }
}