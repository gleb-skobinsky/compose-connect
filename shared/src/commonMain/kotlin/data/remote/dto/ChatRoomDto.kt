package data.remote.dto

import androidx.compose.runtime.Stable
import domain.model.ConversationUiState
import domain.model.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class ChatRoomDto(
    @SerialName("chatroom_uid")
    val id: String,
    @SerialName("chatroom_name")
    val name: String,
    @SerialName("last_message")
    val lastMessage: String,
    @SerialName("last_sent_user_id")
    val lastUser: User,
    @SerialName("number_of_participants")
    val usersCount: Int,
) {
    fun toConvState() = ConversationUiState(
        id = id,
        channelName = name,
        channelMembers = usersCount,
        initialMessages = emptyList()
    )
}