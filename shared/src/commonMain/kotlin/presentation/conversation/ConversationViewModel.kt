package presentation.conversation

import androidx.compose.runtime.Stable
import com.chirrio.filepicker.ImageWithData
import com.chirrio.filepicker.MPFile
import com.chirrio.filepicker.downscale
import com.chirrio.filepicker.toImageBitmap
import common.Resource
import common.util.uuid
import common.viewmodel.IODispatcher
import common.viewmodel.ViewModelPlatformImpl
import data.remote.dto.MessageDto
import data.repository.MessageRepositoryImpl
import data.repository.RoomRepositoryImpl
import data.transport.WsHandler
import domain.model.Attachments
import domain.model.ConversationUiState
import domain.use_case.rooms.getRoomUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import presentation.SharedAppData
import presentation.SharedAppDataImpl
import presentation.conversation.components.InputSelector


@Stable
class ConversationViewModel(
    shared: SharedAppDataImpl,
    chatId: String = "",
) : ViewModelPlatformImpl(), SharedAppData by shared {
    private val websocketHandler = WsHandler()

    private val _inputSelector = MutableStateFlow(InputSelector.NONE)
    val inputSelector = _inputSelector.asStateFlow()

    fun setInputSelector(selector: InputSelector) {
        _inputSelector.value = selector
    }

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

    private val _detailedImage = MutableStateFlow<Int?>(null)
    val detailedImage = _detailedImage.asStateFlow()

    fun setDetailedImage(index: Int?) {
        if (index == null) {
            _detailedImage.value = index
        } else {
            imagesForUpload.value.images.getOrNull(index)?.let {
                _detailedImage.value = index
            }
        }
    }

    private val _imagesForUpload = MutableStateFlow(Attachments())
    val imagesForUpload = _imagesForUpload.asStateFlow()

    fun removeFromImagesForUpload(index: Int) {
        _imagesForUpload.update {
            val oldImages = it.images.toMutableList()
            oldImages.removeAt(index)
            it.copy(images = oldImages)
        }
    }

    fun setImages(images: List<MPFile<Any>>, context: Any) {
        vmScope.launch(IODispatcher) {
            _imagesForUpload.update { it.copy(isLoading = true) }
            val loadedImages = images.map {
                async {
                    it.readAsBytes()?.let { data ->
                        ImageWithData(uuid(), it, data, data.toImageBitmap(context, it).downscale())
                    }
                }
            }
            _imagesForUpload.update { Attachments(false, loadedImages.awaitAll().filterNotNull()) }
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
                val chat =
                    getRoomUseCase(RoomRepositoryImpl, MessageRepositoryImpl, chatId, currentUser)
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