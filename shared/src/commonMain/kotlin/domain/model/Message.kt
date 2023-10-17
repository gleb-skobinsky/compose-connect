package domain.model

import androidx.compose.runtime.Immutable
import common.util.uuid
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import presentation.common.resourceBindings.drawable_default_user

@Immutable
@Serializable
data class Message(
    @Transient
    val id: String = uuid(),
    val roomId: String, // uuid
    val author: User,
    val content: String,
    val timestamp: LocalDateTime,
    val image: String? = null,
    val authorImage: String = drawable_default_user,
)


