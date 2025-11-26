@file:OptIn(ExperimentalResourceApi::class)

package augmy.interactive.com.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.mp.KoinPlatform

@Serializable
data class BetaSignUpPost(
    val email: String
)

@Serializable
data class BetaSignUpResponse(
    val spots: Int
)

class BaseRepository {
    private val httpClient by lazy { KoinPlatform.getKoin().get<HttpClient>() }

    suspend fun signUpToBeta(email: String) = withContext(Dispatchers.Default) {
        httpClient.post(urlString = "https://dev-platform.augmy.org/api/v1/beta/sign-up") {
            setBody(BetaSignUpPost(email))
        }
    }

    suspend fun getBetaSpots() = withContext(Dispatchers.Default) {
        try {
            httpClient.get(
                urlString = "https://dev-platform.augmy.org/api/v1/beta/sign-up"
            ).body<BetaSignUpResponse>()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

open class SharedViewModel: ViewModel() {

    private val dataManager by lazy { KoinPlatform.getKoin().get<SharedDataManager>() }
    private val repository by lazy { KoinPlatform.getKoin().get<BaseRepository>() }

    /** persistent settings saved locally to a device */
    private val settings = KoinPlatform.getKoin().get<Settings>()

    /** Current configuration specific to this app */
    val localSettings = dataManager.localSettings.asStateFlow()
    private val _betaSpots = MutableStateFlow(0)
    val betaSpots = _betaSpots.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getBetaSpots()?.spots?.let {
                _betaSpots.value = it
            }
        }
    }

    /** Sets the theme of the app */
    fun updateTheme(isDarkTheme: Boolean) {
        viewModelScope.launch {
            val theme = if(isDarkTheme) ThemeChoice.DARK else ThemeChoice.LIGHT
            dataManager.localSettings.update {
                LocalSettings(theme = theme)
            }
            settings[SettingsKeys.KEY_THEME] = theme.name
        }
    }

    fun signUp(email: CharSequence) {
        viewModelScope.launch {
            repository.signUpToBeta(email.toString())
        }
    }

    /** Initializes the application */
    fun initApp() {
        viewModelScope.launch {
            if (dataManager.localSettings.value == null) {
                dataManager.localSettings.value = LocalSettings(
                    theme = ThemeChoice.entries.find {
                        it.name == settings.getStringOrNull(SettingsKeys.KEY_THEME)
                    } ?: ThemeChoice.SYSTEM
                )
            }
        }
    }
}