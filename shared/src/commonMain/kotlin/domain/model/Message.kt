package domain.model

import androidx.compose.runtime.Immutable
import common.util.uuid
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import presentation.resourceBindings.drawable_default_user

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


