package augmy.interactive.com.tracking

import augmy.interactive.com.network.adTrackingAuth
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.isSuccess

internal class AdTrackingRepository(
    private val httpClient: HttpClient
) {

    suspend fun track(
        request: AdTrackingRequest
    ): Result<AdTrackingResponse> {
        return runCatching {

            val response = httpClient.post(
                "/api/v1/ad-tracking"
            ) {
                adTrackingAuth()
                setBody(request)
            }

            if (!response.status.isSuccess()) {
                error(
                    "Ad tracking failed: ${response.status}"
                )
            }

            response.body<AdTrackingResponse>()
        }
    }
}