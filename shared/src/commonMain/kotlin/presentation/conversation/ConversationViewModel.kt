package presentation.conversation

import common.Resource
import common.viewmodel.IODispatcher
import common.viewmodel.ViewModelPlatformImpl
import data.repository.MessageRepositoryImpl
import data.repository.RoomRepositoryImpl
import data.transport.WsHandler
import domain.model.ConversationUiState
import domain.model.Message
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
    shared: SharedAppDataImpl
): ViewModelPlatformImpl(), SharedAppData by shared {
    private val websocketHandler = WsHandler()

    private val _currentConversation = MutableStateFlow(ConversationUiState.Empty)
    val currentConversation: StateFlow<ConversationUiState> = _currentConversation.asStateFlow()

    fun sendMessage(message: Message) {
        vmScope.launch {
            websocketHandler.sendMessage(currentConversation.value.id, message)
        }
    }

    init {
        chatId.onEach { id ->
            if (id.isNotEmpty()) {
                vmScope.launch(IODispatcher) {
                    websocketHandler.dropOtherConnections(id)
                    websocketHandler.connectRoom(id) { message ->
                        currentConversation.value.addMessage(message)
                    }
                }
                val chat = getRoomUseCase(RoomRepositoryImpl, MessageRepositoryImpl, id, user.value)
                chat.onEach {
                    when (it) {
                        is Resource.Data -> _currentConversation.value = it.payload
                        is Resource.Loading -> Unit
                        is Resource.Error -> setErrorMessage(it.message)
                    }
                }.launchIn(vmScope)
            }
        }.launchIn(vmScope)
    }
}