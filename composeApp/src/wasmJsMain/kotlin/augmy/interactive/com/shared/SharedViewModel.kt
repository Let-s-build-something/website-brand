package augmy.interactive.com.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import data.io.app.SettingsKeys
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform

/** Viewmodel with shared behavior and injections for general purposes */
open class SharedViewModel: ViewModel() {

    /** Singleton data manager to keep session-only data alive */
    protected val dataManager: SharedDataManager = KoinPlatform.getKoin().get()

    /** persistent settings saved locally to a device */
    protected val settings = KoinPlatform.getKoin().get<Settings>()

    /** Current configuration specific to this app */
    val localSettings = dataManager.localSettings.asStateFlow()

    /** whether toolbar is currently expanded */
    val isToolbarExpanded = dataManager.isToolbarExpanded

    /** Changes the state of the toolbar */
    fun changeToolbarState(expand: Boolean) {
        dataManager.isToolbarExpanded.value = expand
    }

    /** Sets the theme of the app */
    fun updateTheme(isDarkTheme: Boolean) {
        viewModelScope.launch {
            val theme = if(isDarkTheme) ThemeChoice.DARK else ThemeChoice.LIGHT
            dataManager.localSettings.update {
                it?.copy(theme = theme)
            }
            settings[SettingsKeys.KEY_THEME] = theme.name
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