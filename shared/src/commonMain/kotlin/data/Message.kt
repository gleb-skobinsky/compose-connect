package data

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable
import resourceBindings.drawable_default_user

@Immutable
@Serializable
data class Message(
    val author: String,
    val content: String,
    val timestamp: String,
    val image: String? = null,
    val authorImage: String = drawable_default_user,
)