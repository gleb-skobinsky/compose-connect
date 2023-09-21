package data

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import resourceBindings.drawable_default_user
import util.uuid

@Immutable
@Serializable
data class Message(
    @Transient
    val id: String = uuid(),
    val roomId: String,
    val author: String,
    val content: String,
    val timestamp: String,
    val image: String? = null,
    val authorImage: String = drawable_default_user,
)