package data.remote.dto

import domain.model.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    @SerialName("chatroom_id")
    val chatRoom: String,
    @SerialName("user_id")
    val user: User,
    val text: String,
    @SerialName("created_at")
    val timestamp: String,
)
