package presentation.profile

import androidx.compose.runtime.Stable
import common.Resource
import common.viewmodel.ViewModelPlatformImpl
import data.ProfileScreenState
import data.repository.RemoteUserRepository
import domain.use_case.users.getUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import presentation.SharedAppData
import presentation.SharedAppDataImpl

@Stable
class ProfileViewModel(
    shared: SharedAppDataImpl,
    profileId: String = ""
) : ViewModelPlatformImpl(), SharedAppData by shared {
    private val _currentProfile = MutableStateFlow<ProfileScreenState?>(null)
    val currentProfile: StateFlow<ProfileScreenState?> = _currentProfile.asStateFlow()

    init {
        if (profileId.isNotEmpty()) {
            user.value?.let { currentUser ->
                val fetchedProfile = getUserUseCase(RemoteUserRepository, profileId, currentUser)
                fetchedProfile.onEach {
                    when (it) {
                        is Resource.Data -> withSuccess { _currentProfile.value = it.payload }
                        is Resource.Loading -> Unit
                        is Resource.Error -> setErrorMessage(it)
                    }
                }.launchIn(vmScope)
            }
        }
    }
}