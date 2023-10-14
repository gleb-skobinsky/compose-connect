package data

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import presentation.resourceBindings.drawable_default_user
import common.util.uuid

@Immutable
@Serializable
data class Message(
    @Transient
    val id: String = uuid(),
    val roomId: String, // uid
    val author: User,
    val content: String,
    val timestamp: String,
    val image: String? = null,
    val authorImage: String = drawable_default_user,
)

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

typealias InitialMessages = List<MessageDto>

fun InitialMessages.toConvState() = map {
    Message(roomId = it.chatRoom, author = it.user, content = it.text, timestamp = it.timestamp)
}

