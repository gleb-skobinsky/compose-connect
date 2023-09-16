package data

import androidx.compose.runtime.Stable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import themes.ThemeMode
import transport.WsHandler
import viewmodel.ViewModelPlatformImpl

@Stable
class MainViewModel : ViewModelPlatformImpl() {
    private val connectionsHandler = WsHandler()

    init {
        vmScope.launch(Dispatchers.Default) {
            connectionsHandler.connectRoom("chat/composers/") { message ->
                _conversationUiState.value.addMessage(message)
            }
        }
    }

    private val _user: MutableStateFlow<User?> = MutableStateFlow(null)
    val user = _user.asStateFlow()

    fun setUser(user: User) {
        _user.value = user
    }

    private val _loginScreenMode = MutableStateFlow(LoginScreenState.LOGIN)
    val loginScreenMode = _loginScreenMode.asStateFlow()

    fun setLoginMode(mode: LoginScreenState) {
        _loginScreenMode.value = mode
    }

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
            connectionsHandler.sendMessage(message)
        }
    }

    fun loginUser(email: String, password: String) {

    }
}

enum class AppScreenState {
    CHAT,
    ACCOUNT
}

enum class LoginScreenState {
    LOGIN,
    REGISTER
}