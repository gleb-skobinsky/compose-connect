package data

import androidx.compose.runtime.Stable
import data.repositories.MessagesRepository
import data.repositories.RoomRepository
import data.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import themes.ThemeMode
import transport.WsHandler
import util.uuid
import viewmodel.ViewModelPlatformImpl

@Stable
class MainViewModel : ViewModelPlatformImpl() {
    private val websocketHandler = WsHandler()

    private val _plusRoomDialogOpen = MutableStateFlow(false)
    val plusRoomDialogOpen = _plusRoomDialogOpen.asStateFlow()

    fun openRoomDialog() {
        _plusRoomDialogOpen.value = true
    }

    fun closeRoomDialog() {
        _plusRoomDialogOpen.value = false
    }

    private val _user: MutableStateFlow<User> = MutableStateFlow(User.Empty)
    val user = _user.asStateFlow()

    private val _loginScreenMode = MutableStateFlow(LoginScreenState.LOGIN)
    val loginScreenMode = _loginScreenMode.asStateFlow()

    fun setLoginMode(mode: LoginScreenState) {
        _loginScreenMode.value = mode
    }

    private val _errorMessage = MutableStateFlow<Resource.Error<*>?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _screenState: MutableStateFlow<AppScreenState> = MutableStateFlow(AppScreenState.CHAT)
    val screenState: StateFlow<AppScreenState> = _screenState.asStateFlow()

    private val _chats: MutableStateFlow<Map<String, ConversationUiState>> = MutableStateFlow(emptyMap())
    val chats = _chats.asStateFlow()

    private val _conversationUiState: MutableStateFlow<ConversationUiState> = MutableStateFlow(ConversationUiState.Empty)
    val conversationUiState = _conversationUiState.asStateFlow()

    private val _selectedUserProfile: MutableStateFlow<ProfileScreenState?> = MutableStateFlow(null)
    val selectedUserProfile: StateFlow<ProfileScreenState?> = _selectedUserProfile.asStateFlow()

    private val _themeMode: MutableStateFlow<ThemeMode> = MutableStateFlow(ThemeMode.DARK)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    private val _drawerShouldBeOpened: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val drawerShouldBeOpened: StateFlow<Boolean> = _drawerShouldBeOpened.asStateFlow()

    private val _searchedUsers = MutableStateFlow(emptyList<User>())
    val searchedUsers = _searchedUsers.asStateFlow()

    private val _selectedUsers = MutableStateFlow(emptySet<String>())
    val selectedUsers = _selectedUsers.asStateFlow()

    fun selectUser(email: String) {
        _selectedUsers.update { it + email }
    }

    fun unselectUser(email: String) {
        _selectedUsers.update { it - email }
    }

    fun setCurrentConversation(id: String) {
        _screenState.value = AppScreenState.CHAT
        _conversationUiState.value = chats.value.getValue(id)
        vmScope.launch(Dispatchers.Default) {
            websocketHandler.dropOtherConnections(id)
            websocketHandler.connectRoom(id) { message ->
                _conversationUiState.value.addMessage(message)
            }
        }
        vmScope.launch {
            when (val messages = MessagesRepository.getMessagesForRoom(id, user.value)) {
                is Resource.Data -> {
                    _chats.update {
                        val oldRoom: ConversationUiState = it.getValue(id)
                        val newRoom = oldRoom.copy(initialMessages = messages.payload.toConvState())
                        _conversationUiState.value = newRoom
                        it + mapOf(newRoom.id to newRoom)
                    }
                }

                is Resource.Error -> Unit
            }
        }
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
            websocketHandler.sendMessage(conversationUiState.value.id, message)
        }
    }

    fun loginUser(email: String, password: String) {
        vmScope.launch {
            when (val result = UserRepository.login(email, password)) {
                is Resource.Data -> {
                    _user.value = result.payload
                    when (val rooms = RoomRepository.getRoomsByUser(result.payload)) {
                        is Resource.Data -> _chats.value = rooms.payload
                        is Resource.Error -> {
                            println(rooms.message)
                            println(rooms.status)
                        }
                    }
                }

                is Resource.Error -> {
                    _user.value = User.Empty
                    _errorMessage.value = result
                }
            }
        }
    }

    fun logoutUser() {
        vmScope.launch {
            UserRepository.logout(user.value)
            _user.value = User.Empty
            resetAllStates()
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

    fun createRoom(roomName: String) {
        vmScope.launch {
            when (val room = RoomRepository.createRoom(
                room = ChatRoomCreationDto(
                    id = uuid(),
                    name = roomName,
                    users = listOf(user.value.email) + selectedUsers.value.toList()
                ),
                currentUser = user.value
            )
            ) {
                is Resource.Data<ChatRoomCreationDto> -> {
                    val new = mapOf(
                        room.payload.id to room.payload.toConvState()
                    )
                    _chats.update { it + new }
                }

                is Resource.Error<ChatRoomCreationDto> -> Unit
            }
            closeRoomDialog()
        }
    }

    fun searchUsers(email: String) {
        vmScope.launch {
            when (val searched = UserRepository.search(SearchUser(email), _user.value)) {
                is Resource.Data -> {
                    _searchedUsers.value = searched.payload.users
                }

                is Resource.Error -> {
                    println(searched.message)
                    println(searched.status)
                }
            }
        }
    }

    fun clearSearch() {
        _searchedUsers.value = emptyList()
    }

    operator fun get(chatRoomId: String?): ConversationUiState =
        chatRoomId?.let {
            _chats.value[it]
        } ?: ConversationUiState.Empty

    private fun resetAllStates() {
        _loginScreenMode.value = ViewModelDefaults<LoginScreenState>()
        _errorMessage.value = ViewModelDefaults<Resource.Error<*>?>()
        _screenState.value = ViewModelDefaults<AppScreenState>()
        _chats.value = ViewModelDefaults<Map<String, ConversationUiState>>()
        _conversationUiState.value = ViewModelDefaults<ConversationUiState>()
        _selectedUserProfile.value = ViewModelDefaults<ProfileScreenState?>()
        _searchedUsers.value = ViewModelDefaults<List<User>>()
        _selectedUsers.value = ViewModelDefaults<Set<String>>()
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