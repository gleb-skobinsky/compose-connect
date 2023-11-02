package presentation.login_screen

import androidx.compose.runtime.Stable
import com.chirrio.filepicker.ImageWithData
import com.chirrio.filepicker.toByteArray
import common.Resource
import common.viewmodel.ViewModelPlatformImpl
import data.repository.RemoteUserRepository
import domain.use_case.users.loginUseCase
import domain.use_case.users.signupUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import navigation.Screens
import navigation.SharedNavigator
import navigation.navigateTo
import presentation.SharedAppData
import presentation.SharedAppDataImpl

@Stable
class LoginViewModel(
    shared: SharedAppDataImpl,
) : ViewModelPlatformImpl(), SharedAppData by shared {

    private val _userImage = MutableStateFlow<ImageWithData?>(null)
    val userImage = _userImage.asStateFlow()

    fun setUserImage(image: ImageWithData) {
        _userImage.value = image
    }

    fun loginUser(email: String, password: String, navHost: SharedNavigator?) {
        val result = loginUseCase(RemoteUserRepository, email, password)
        result.onEach { resource ->
            when (resource) {
                is Resource.Data -> withSuccess {
                    setUser(resource.payload)
                    navHost?.navigateTo(Screens.Main())
                }

                is Resource.Error -> {
                    setUser(null)
                    setErrorMessage(resource)
                }

                is Resource.Loading -> Unit
            }
        }.launchIn(vmScope)
    }

    fun signupUser(
        email: String,
        firstName: String,
        lastName: String,
        password: String,
        navHost: SharedNavigator?
    ) {
        val ext = userImage.value?.file?.path?.substringAfterLast(".")
        val result = signupUseCase(
            repository = RemoteUserRepository,
            email = email,
            firstName = firstName,
            lastName = lastName,
            password = password,
            image = userImage.value?.imageBitmap?.toByteArray(),
            fileExtension = ext ?: ""
        )
        result.onEach {
            when (it) {
                is Resource.Data -> withSuccess { setUser(it.payload); navHost?.navigateTo(Screens.Main()) }
                is Resource.Error -> setErrorMessage(it)
                is Resource.Loading -> Unit
            }
        }.launchIn(vmScope)
    }
}