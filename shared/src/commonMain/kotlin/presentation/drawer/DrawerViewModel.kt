package presentation.drawer

import common.Resource
import common.viewmodel.ViewModelPlatformImpl
import data.repository.RoomRepositoryImpl
import data.repository.UserRepositoryImpl
import domain.model.User
import domain.use_case.rooms.createRoomUseCase
import domain.use_case.rooms.getRooms
import domain.use_case.users.logoutUseCase
import domain.use_case.users.searchUsersUseCase
import kotlinx.coroutines.flow.*
import presentation.SharedAppData
import presentation.SharedAppDataImpl

class DrawerViewModel(
    shared: SharedAppDataImpl,
) : ViewModelPlatformImpl(), SharedAppData by shared {

    private val _chatId = MutableStateFlow("")
    val chatId = _chatId.asStateFlow()

    fun setChatId(id: String) {
        _chatId.value = id
    }

    private val _userId = MutableStateFlow("")
    val userId = _userId.asStateFlow()

    fun setUserId(id: String) {
        _userId.value = id
    }

    private val _plusRoomDialogOpen = MutableStateFlow(false)
    val plusRoomDialogOpen = _plusRoomDialogOpen.asStateFlow()

    fun openRoomDialog() {
        _plusRoomDialogOpen.value = true
    }

    fun closeRoomDialog() {
        _plusRoomDialogOpen.value = false
    }

    private val _chats: MutableStateFlow<Map<String, String>> = MutableStateFlow(emptyMap())
    val chats = _chats.asStateFlow()

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

    fun resetOpenDrawerAction() {
        _drawerShouldBeOpened.value = false
    }

    fun createRoom(roomName: String) {
        user.value?.let { currentUser ->
            val result =
                createRoomUseCase(RoomRepositoryImpl, roomName, selectedUsers.value, currentUser)
            result.onEach { conversation ->
                when (conversation) {
                    is Resource.Data -> _chats.update { it + conversation.payload }
                    is Resource.Loading -> Unit
                    is Resource.Error -> setErrorMessage(conversation.message)
                }
            }.launchIn(vmScope)
            closeRoomDialog()
        }
    }

    fun searchUsers(email: String) {
        user.value?.let { currentUser ->
            val result = searchUsersUseCase(UserRepositoryImpl, email, currentUser)
            result.onEach {
                when (it) {
                    is Resource.Data -> _searchedUsers.value = it.payload
                    is Resource.Error -> setErrorMessage(it.message)
                    is Resource.Loading -> Unit
                }
            }.launchIn(vmScope)
        }
    }

    fun clearSearch() {
        _searchedUsers.value = emptyList()
    }

    fun logoutUser() {
        user.value?.let { currentUser ->
            val result = logoutUseCase(UserRepositoryImpl, currentUser)
            result.onEach {
                if (it == null) {
                    setUser(null)
                } else {
                    setUser(null)
                    setErrorMessage(it)
                }
            }.launchIn(vmScope)
        }
    }

    init {
        user.filterNotNull().onEach {
            val result = getRooms(RoomRepositoryImpl, it)
            result.onEach { rooms ->
                when (rooms) {
                    is Resource.Data -> _chats.value = rooms.payload
                    is Resource.Error -> setErrorMessage(rooms.message)
                    is Resource.Loading -> Unit
                }
            }.launchIn(vmScope)
        }.launchIn(vmScope)
    }
}