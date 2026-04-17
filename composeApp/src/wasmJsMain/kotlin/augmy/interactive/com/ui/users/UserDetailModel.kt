package augmy.interactive.com.ui.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import augmy.interactive.com.data.NetworkItemIO
import augmy.interactive.com.shared.BaseRepository
import augmy.interactive.com.shared.BaseResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform

class UserDetailModel(private val userId: String?): ViewModel() {
    private val repository by lazy { KoinPlatform.getKoin().get<BaseRepository>() }
    private val _response = MutableStateFlow<BaseResponse<NetworkItemIO>>(BaseResponse.Idle)
    val response = _response.asStateFlow()

    init {
        userId?.takeIf { it.isNotBlank() }?.let { userId ->
            viewModelScope.launch {
                _response.value = repository.getRemoteUser(userId)
            }
        }
    }
}
