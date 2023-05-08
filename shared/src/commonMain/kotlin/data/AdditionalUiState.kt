package data

import androidx.compose.runtime.Stable
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.json.Json
import themes.ThemeMode
import transport.getLocalHost
import kotlin.coroutines.EmptyCoroutineContext

@Stable
class AdditionalUiState {
    private val scope = CoroutineScope(EmptyCoroutineContext)
    private val client = HttpClient {
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }
    }
    private var session: DefaultClientWebSocketSession? = null

    init {
        scope.launch {
            withContext(Dispatchers.Default) {
                client.webSocket(method = HttpMethod.Get, host = getLocalHost(), port = 8082) {
                    session = this
                    while (true) {
                        this.ensureActive()
                    }
                }
            }
        }
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
    fun openDrawer() {
        _drawerShouldBeOpened.value = true
    }

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
        _conversationUiState.value.addMessage(message)
        scope.launch {
            session?.sendSerialized(message)
        }
    }
}

enum class AppScreenState {
    CHAT,
    ACCOUNT
}