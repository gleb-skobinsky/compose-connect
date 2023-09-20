package data

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class ChatRoomCreationDto(
    val id: String,
    val name: String,
    val users: List<String>, // list of emails
) {
    fun toConvState() = ConversationUiState(
        channelName = name,
        channelMembers = users.size,
        initialMessages = emptyList()
    )
}