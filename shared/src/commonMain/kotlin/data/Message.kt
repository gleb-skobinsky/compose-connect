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
    val author: String, // user email
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
    val userEmail: String,
    val text: String,
    val timestamp: String,
)

@Stable
@Serializable
data class InitialMessages(
    val messages: List<MessageDto>,
) {
    fun toConvState() = messages.map {
        Message(roomId = it.chatRoom, author = it.userEmail, content = it.text, timestamp = it.timestamp)
    }
}