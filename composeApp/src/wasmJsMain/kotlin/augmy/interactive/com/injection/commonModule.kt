package augmy.interactive.com.injection

import augmy.interactive.com.shared.SharedDataManager
import augmy.interactive.com.shared.SharedViewModel
import coil3.annotation.ExperimentalCoilApi
import coil3.network.CacheStrategy
import coil3.network.NetworkFetcher
import coil3.network.ktor3.asNetworkClient
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/** Common module for the whole application */
@OptIn(ExperimentalCoilApi::class)
internal val commonModule = module {
    single { SharedDataManager() }
    single { Settings() }
    viewModelOf(::SharedViewModel)

    single {
        NetworkFetcher.Factory(
            networkClient = { get<HttpClient>().asNetworkClient() },
            cacheStrategy = { CacheStrategy() },
        )
    }
}