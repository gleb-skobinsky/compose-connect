package data

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable
import resourceBindings.drawable_ali
import resourceBindings.drawable_someone_else

@Immutable
@Serializable
data class Message(
    val author: String,
    val content: String,
    val timestamp: String,
    val image: String? = null,
    val authorImage: String = if (author == "me") drawable_ali else drawable_someone_else,
)