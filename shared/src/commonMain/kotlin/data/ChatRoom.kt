package data

import androidx.compose.runtime.Stable
import kotlinx.serialization.SerialName
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

@Stable
@Serializable
data class ChatRoomFromDb(
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
        channelName = name,
        channelMembers = usersCount,
        initialMessages = emptyList()
    )
}

@Stable
@Serializable
data class UserRooms(
    val rooms: List<ChatRoomFromDb> = emptyList(),
) {
    fun toConvState() = rooms.associate {
        it.id to it.toConvState()
    }
}