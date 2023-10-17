package data.remote.dto

import common.util.toLocal
import common.util.uuid
import domain.model.Message
import domain.model.User
import kotlinx.datetime.Instant
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
    val timestamp: Instant,
) {
    fun toMessage(): Message = Message(
        id = uuid(),
        roomId = chatRoom,
        author = user,
        content = text,
        timestamp = timestamp.toLocal()
    )
}
