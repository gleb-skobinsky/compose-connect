package domain.model

import androidx.compose.runtime.Stable
import androidx.compose.runtime.toMutableStateList

@Stable
data class ConversationUiState(
    val id: String,
    val channelName: String,
    val channelMembers: Int,
    val initialMessages: List<Message>,
) {
    private val _messages: MutableList<Message> = initialMessages.toMutableStateList()
    val messages: List<Message> = _messages

    fun addMessage(msg: Message) {
        _messages.add(0, msg) // Add to the beginning of the list
    }
}

