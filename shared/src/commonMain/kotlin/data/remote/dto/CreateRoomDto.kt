package data.remote.dto

import androidx.compose.runtime.Stable
import domain.model.ConversationUiState
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class CreateRoomDto(
    val id: String,
    val name: String,
    val users: List<String>, // list of emails
) {
    fun toConvState() = ConversationUiState(
        id = id,
        channelName = name,
        channelMembers = users.size,
        initialMessages = emptyList()
    )
}