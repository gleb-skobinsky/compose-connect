package domain.model

import androidx.compose.runtime.Immutable
import common.util.uuid
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Immutable
@Serializable
data class Message(
    @Transient
    val id: String = uuid(),
    val roomId: String, // uuid
    val author: User,
    val content: String,
    val timestamp: LocalDateTime,
    val image: String? = null
)


