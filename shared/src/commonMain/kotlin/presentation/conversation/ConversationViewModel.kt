package presentation.conversation

import common.Resource
import common.viewmodel.IODispatcher
import common.viewmodel.ViewModelPlatformImpl
import data.remote.dto.MessageDto
import data.repository.MessageRepositoryImpl
import data.repository.RoomRepositoryImpl
import data.transport.WsHandler
import domain.model.ConversationUiState
import domain.use_case.rooms.getRoomUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import presentation.SharedAppData
import presentation.SharedAppDataImpl

class ConversationViewModel(
    shared: SharedAppDataImpl,
    chatId: String = "",
) : ViewModelPlatformImpl(), SharedAppData by shared {
    private val websocketHandler = WsHandler()

    private val _currentConversation = MutableStateFlow<ConversationUiState?>(null)
    val currentConversation: StateFlow<ConversationUiState?> = _currentConversation.asStateFlow()

    fun sendMessage(message: MessageDto) {
        currentConversation.value?.addMessage(message.toMessage())
        currentConversation.value?.let {
            vmScope.launch {
                websocketHandler.sendMessage(it.id, message)
            }
        }
    }

    init {
        if (chatId.isNotEmpty()) {
            user.value?.let { currentUser ->
                vmScope.launch(IODispatcher) {
                    websocketHandler.dropOtherConnections(chatId)
                    websocketHandler.connectRoom(chatId) { message ->
                        currentConversation.value?.addMessage(message.toMessage())
                    }
                }
                val chat = getRoomUseCase(RoomRepositoryImpl, MessageRepositoryImpl, chatId, currentUser)
                chat.onEach {
                    when (it) {
                        is Resource.Data -> withSuccess { _currentConversation.value = it.payload }
                        is Resource.Loading -> Unit
                        is Resource.Error -> setErrorMessage(it)
                    }
                }.launchIn(vmScope)
            }
        }
    }
}