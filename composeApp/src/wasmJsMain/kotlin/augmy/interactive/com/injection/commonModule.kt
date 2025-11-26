package augmy.interactive.com.injection

import augmy.interactive.com.BuildKonfig
import augmy.interactive.com.shared.BaseRepository
import augmy.interactive.com.shared.SharedDataManager
import augmy.interactive.com.shared.SharedViewModel
import coil3.annotation.ExperimentalCoilApi
import coil3.network.NetworkFetcher
import coil3.network.ktor3.asNetworkClient
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

@OptIn(ExperimentalCoilApi::class)
internal val commonModule = module {
    single { SharedDataManager() }
    factory { BaseRepository() }
    single { Settings() }
    viewModelOf(::SharedViewModel)
    single<HttpClient> {
        HttpClient().config {
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                        coerceInputValues = true
                    }
                )
            }

            defaultRequest {
                header(HttpHeaders.Authorization, "Bearer ${BuildKonfig.BearerToken}")
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)

                url {
                    protocol = URLProtocol.HTTPS
                }
            }
            install(HttpSend)
        }
    }

    single {
        NetworkFetcher.Factory(
            networkClient = { get<HttpClient>().asNetworkClient() }
        )
    }
}