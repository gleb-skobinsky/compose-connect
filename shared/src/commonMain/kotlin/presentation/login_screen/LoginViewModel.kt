package presentation.login_screen

import common.Resource
import common.viewmodel.ViewModelPlatformImpl
import data.repository.UserRepositoryImpl
import domain.use_case.users.loginUseCase
import domain.use_case.users.signupUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import presentation.SharedAppData
import presentation.SharedAppDataImpl

class LoginViewModel(
    shared: SharedAppDataImpl,
) : ViewModelPlatformImpl(), SharedAppData by shared {

    fun loginUser(email: String, password: String) {
        val result = loginUseCase(UserRepositoryImpl, email, password)
        result.onEach { resource ->
            when (resource) {
                is Resource.Data -> {
                    setUser(resource.payload)
                }

                is Resource.Error -> {
                    setUser(null)
                    setErrorMessage(resource.message)
                }

                is Resource.Loading -> Unit
            }
        }.launchIn(vmScope)
    }

    fun signupUser(email: String, firstName: String, lastName: String, password: String) {
        val result = signupUseCase(UserRepositoryImpl, email, firstName, lastName, password)
        result.onEach {
            when (it) {
                is Resource.Data -> setUser(it.payload)
                is Resource.Error -> setErrorMessage(it.message)
                is Resource.Loading -> Unit
            }
        }.launchIn(vmScope)
    }
}