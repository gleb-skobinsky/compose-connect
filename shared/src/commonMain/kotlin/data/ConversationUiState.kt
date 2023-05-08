package data

import androidx.compose.runtime.mutableStateListOf

class ConversationUiState(
    val channelName: String,
    val channelMembers: Int,
    initialMessages: List<Message>,
) {
    private val _messages: MutableList<Message> =
        mutableStateListOf(*initialMessages.toTypedArray())
    val messages: List<Message> = _messages

    fun addMessage(msg: Message) {
        _messages.add(0, msg) // Add to the beginning of the list
    }
}
