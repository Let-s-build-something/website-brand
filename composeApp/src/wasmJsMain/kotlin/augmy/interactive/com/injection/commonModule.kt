package augmy.interactive.com.injection

import augmy.interactive.com.shared.SharedDataManager
import augmy.interactive.com.shared.SharedViewModel
import coil3.annotation.ExperimentalCoilApi
import coil3.network.NetworkFetcher
import coil3.network.ktor3.asNetworkClient
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/** Common module for the whole application */
@OptIn(ExperimentalCoilApi::class)
internal val commonModule = module {
    single { SharedDataManager() }
    single { Settings() }
    viewModelOf(::SharedViewModel)

    single {
        HttpClient(Js) {
            install(HttpTimeout) {
                requestTimeoutMillis = 15000
                connectTimeoutMillis = 5000
                socketTimeoutMillis = 10000
            }
            HttpResponseValidator {
                validateResponse { response ->
                    val status = response.status.value
                    if (status !in 200..299) {
                        throw ClientRequestException(response, "HTTP error: $status")
                    }
                }
            }

            expectSuccess = false
        }
    }

    single {
        NetworkFetcher.Factory(
            networkClient = { get<HttpClient>().asNetworkClient() }
        )
    }
}