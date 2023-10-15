package presentation.conversation

import common.Resource
import common.viewmodel.IODispatcher
import common.viewmodel.ViewModelPlatformImpl
import data.repository.MessageRepositoryImpl
import data.transport.WsHandler
import domain.model.ConversationUiState
import domain.model.Message
import domain.use_case.messages.getMessagesUseCase
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import presentation.SharedViewModel
import presentation.SharedViewModelImpl

class ConversationViewModel(
    shared: SharedViewModelImpl
): ViewModelPlatformImpl(), SharedViewModel by shared {
    private val websocketHandler = WsHandler()

    fun sendMessage(message: Message) {
        vmScope.launch {
            websocketHandler.sendMessage(currentConversation.value.id, message)
        }
    }

    init {
        currentConversation.distinctUntilChangedBy { it.id }.onEach {  conversation ->
            if (conversation != ConversationUiState.Empty) {
                val id = conversation.id
                vmScope.launch(IODispatcher) {
                    websocketHandler.dropOtherConnections(id)
                    websocketHandler.connectRoom(id) { message ->
                        currentConversation.value.addMessage(message)
                    }
                }
                val messages = getMessagesUseCase(MessageRepositoryImpl, id, user.value)
                messages.onEach {
                    when (it) {
                        is Resource.Data -> {
                            val oldRoom = currentConversation.value
                            val newRoom = oldRoom.copy(initialMessages = it.payload)
                            setCurrentConversation(newRoom)
                        }

                        is Resource.Loading -> Unit
                        is Resource.Error -> setErrorMessage(it.message)
                    }
                }.launchIn(vmScope)
            }
        }.launchIn(vmScope)
    }
}