package augmy.interactive.com.shared

import kotlinx.coroutines.flow.MutableStateFlow

/** Shared data manager with most common information */
class SharedDataManager {

    /** whether toolbar is currently expanded */
    val isToolbarExpanded = MutableStateFlow(true)

    /** Current configuration specific to this app */
    val localSettings = MutableStateFlow<LocalSettings?>(null)
}