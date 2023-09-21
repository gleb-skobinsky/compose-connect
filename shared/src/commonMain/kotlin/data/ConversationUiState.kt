package data

import androidx.compose.runtime.Stable
import androidx.compose.runtime.toMutableStateList

@Stable
class ConversationUiState(
    val id: String,
    val channelName: String,
    val channelMembers: Int,
    initialMessages: List<Message>,
) {
    private val _messages: MutableList<Message> = initialMessages.toMutableStateList()
    val messages: List<Message> = _messages

    fun addMessage(msg: Message) {
        _messages.add(0, msg) // Add to the beginning of the list
    }

    companion object {
        val Empty = ConversationUiState(
            id = "",
            channelName = "",
            channelMembers = 0,
            initialMessages = emptyList()
        )
    }
}

