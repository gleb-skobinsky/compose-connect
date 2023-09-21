package data

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import resourceBindings.drawable_default_user
import util.uuid

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
    val timestamp: String,
)

@Stable
@Serializable
data class InitialMessages(
    val messages: List<MessageDto>,
) {
    fun toConvState() = messages.map {
        Message(roomId = it.chatRoom, author = it.user, content = it.text, timestamp = it.timestamp)
    }
}