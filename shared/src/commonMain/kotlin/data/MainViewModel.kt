package data

import androidx.compose.runtime.Stable
import buildVariant.mode
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import themes.ThemeMode
import transport.Routes
import transport.WsHandler
import transport.platformName
import viewmodel.ViewModelPlatformImpl
import kotlin.coroutines.cancellation.CancellationException

@Stable
class MainViewModel : ViewModelPlatformImpl() {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }
    private val websocketHandler = WsHandler()

    init {
        vmScope.launch(Dispatchers.Default) {
            websocketHandler.connectRoom("chat/composers/") { message ->
                _conversationUiState.value.addMessage(message)
            }
        }
    }

    private val _user: MutableStateFlow<User?> = MutableStateFlow(null)
    val user = _user.asStateFlow()

    private fun setUser(user: User) {
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
            websocketHandler.sendMessage(message)
        }
    }

    fun loginUser(email: String, password: String) {
        vmScope.launch {
            try {

                val response = httpClient.post("http://${Routes[mode][platformName]}/token/") {
                    contentType(ContentType.Application.Json)
                    setBody(LoginForm(email, password))
                }
                if (response.status.value in 200..299) {
                    val credentials = Json.decodeFromString<CredentialsResponse>(response.bodyAsText())
                    setUser(
                        User(email = email, accessToken = credentials.access, refreshToken = credentials.refresh)
                    )
                } else {
                    throw CancellationException("Wrong response status: ${response.status}")
                }
            } catch (e: Exception) {
                println("Error connecting to server")
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