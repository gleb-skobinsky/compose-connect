package data

import androidx.compose.runtime.Immutable

@Immutable
data class ProfileScreenState(
    val userId: String,
    val photo: String?,
    val name: String,
    val status: String,
    val displayName: String,
    val position: String,
    val twitter: String = "",
    val timeZone: String?, // Null if me
    val commonChannels: String?, // Null if me
) {
    fun isMe() = userId == meProfile.userId
}

