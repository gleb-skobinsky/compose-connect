package presentation.drawer

import common.Resource
import common.viewmodel.ViewModelPlatformImpl
import domain.model.ConversationUiState
import data.ProfileScreenState
import data.exampleAccountsState
import data.repository.RoomRepositoryImpl
import data.repository.UserRepositoryImpl
import domain.model.AppScreenState
import domain.model.User
import domain.use_case.rooms.createRoomUseCase
import domain.use_case.rooms.getRooms
import domain.use_case.users.logoutUseCase
import domain.use_case.users.searchUsersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import presentation.SharedViewModel

class DrawerViewModel(
    shared: SharedViewModel
) : ViewModelPlatformImpl(), SharedViewModel by shared {

    private val _selectedUserProfile: MutableStateFlow<ProfileScreenState?> = MutableStateFlow(null)
    val selectedUserProfile: StateFlow<ProfileScreenState?> = _selectedUserProfile.asStateFlow()

    private val _plusRoomDialogOpen = MutableStateFlow(false)
    val plusRoomDialogOpen = _plusRoomDialogOpen.asStateFlow()

    fun openRoomDialog() {
        _plusRoomDialogOpen.value = true
    }

    fun closeRoomDialog() {
        _plusRoomDialogOpen.value = false
    }

    private val _chats: MutableStateFlow<Map<String, ConversationUiState>> = MutableStateFlow(emptyMap())
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

    fun setCurrentAccount(userId: String) {
        setScreenState(AppScreenState.ACCOUNT)
        _selectedUserProfile.value = exampleAccountsState.getValue(userId)
    }

    fun resetOpenDrawerAction() {
        _drawerShouldBeOpened.value = false
    }

    fun createRoom(roomName: String) {
        val result = createRoomUseCase(RoomRepositoryImpl, roomName, selectedUsers.value, user.value)
        result.onEach {conversation ->
            when (conversation) {
                is Resource.Data -> _chats.update { it + conversation.payload }
                is Resource.Loading -> Unit
                is Resource.Error -> setErrorMessage(conversation.message)
            }
        }
    }

    fun setCurrentConversation(id: String) {
        setScreenState(AppScreenState.CHAT)
        setCurrentConversation(chats.value.getValue(id))
    }

    fun searchUsers(email: String) {
        val result = searchUsersUseCase(UserRepositoryImpl, email, user.value)
        result.onEach {
            when (it) {
                is Resource.Data -> _searchedUsers.value = it.payload
                is Resource.Error -> setErrorMessage(it.message)
                is Resource.Loading -> Unit
            }
        }.launchIn(vmScope)
    }

    fun clearSearch() {
        _searchedUsers.value = emptyList()
    }

    fun logoutUser() {
        val result = logoutUseCase(UserRepositoryImpl, user.value)
        result.onEach {
            if (it == null) {
                setUser(User.Empty)
            } else {
                setUser(User.Empty)
                setErrorMessage(it)
            }
        }.launchIn(vmScope)
    }

    init {
        user.onEach {
            if (it != User.Empty) {
                val result = getRooms(RoomRepositoryImpl, it)
                result.onEach {  rooms ->
                    when (rooms) {
                        is Resource.Data -> _chats.value = rooms.payload
                        is Resource.Error -> setErrorMessage(rooms.message)
                        is Resource.Loading -> Unit
                    }
                }.launchIn(vmScope)

            }
        }.launchIn(vmScope)
    }
}