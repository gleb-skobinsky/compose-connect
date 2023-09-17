package data

import androidx.compose.runtime.Stable
import data.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import themes.ThemeMode
import transport.WsHandler
import viewmodel.ViewModelPlatformImpl

@Stable
class MainViewModel : ViewModelPlatformImpl() {
    private val websocketHandler = WsHandler()

    init {
        vmScope.launch(Dispatchers.Default) {
            websocketHandler.connectRoom("composers/") { message ->
                _conversationUiState.value.addMessage(message)
            }
        }
    }

    private val _user: MutableStateFlow<User?> = MutableStateFlow(null)
    val user = _user.asStateFlow()

    private val _loginScreenMode = MutableStateFlow(LoginScreenState.LOGIN)
    val loginScreenMode = _loginScreenMode.asStateFlow()

    fun setLoginMode(mode: LoginScreenState) {
        _loginScreenMode.value = mode
    }

    private val _errorMessage = MutableStateFlow<Resource.Error<*>?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _screenState: MutableStateFlow<AppScreenState> = MutableStateFlow(AppScreenState.CHAT)
    val screenState: StateFlow<AppScreenState> = _screenState

    private val _conversationUiState: MutableStateFlow<ConversationUiState> = MutableStateFlow(exampleUiState.getValue("composers"))
    val conversationUiState: StateFlow<ConversationUiState> = _conversationUiState

    private val _selectedUserProfile: MutableStateFlow<ProfileScreenState?> = MutableStateFlow(null)
    val selectedUserProfile: StateFlow<ProfileScreenState?> = _selectedUserProfile

    private val _themeMode: MutableStateFlow<ThemeMode> = MutableStateFlow(ThemeMode.LIGHT)
    val themeMode: StateFlow<ThemeMode> = _themeMode

    private val _drawerShouldBeOpened: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val drawerShouldBeOpened: StateFlow<Boolean> = _drawerShouldBeOpened

    fun setCurrentConversation(title: String) {
        _screenState.value = AppScreenState.CHAT
        _conversationUiState.value = exampleUiState.getValue(title)
    }

    fun setCurrentAccount(userId: String) {
        _screenState.value = AppScreenState.ACCOUNT
        _selectedUserProfile.value = exampleAccountsState.getValue(userId)
    }

    fun resetOpenDrawerAction() {
        _drawerShouldBeOpened.value = false
    }

    fun switchTheme(theme: ThemeMode) {
        _themeMode.value = theme
    }

    fun sendMessage(message: Message) {
        vmScope.launch {
            websocketHandler.sendMessage(message)
        }
    }

    fun loginUser(email: String, password: String) {
        vmScope.launch {
            when (val result = UserRepository.login(email, password)) {
                is Resource.Data -> _user.value = result.payload
                is Resource.Error -> {
                    _user.value = null
                    _errorMessage.value = result
                }
            }
        }
    }

    fun logoutUser() {
        vmScope.launch {
            user.value?.let { user ->
                _user.value = null
                when (val result = UserRepository.logout(user)) {
                    is Resource.Data -> Unit
                    is Resource.Error -> _errorMessage.value = result
                }
            }
        }
    }

    fun signupUser(email: String, firstName: String, lastName: String, password: String) {
        vmScope.launch {
            val result = UserRepository.signupUser(
                SignupRequest(email, firstName, lastName, password)
            )
            when (result) {
                is Resource.Data -> _user.value = result.payload
                is Resource.Error -> _errorMessage.value = result
            }
        }
    }
}

@Serializable
data class LoginForm(val email: String, val password: String)

enum class AppScreenState {
    CHAT,
    ACCOUNT
}

enum class LoginScreenState {
    LOGIN,
    REGISTER
}