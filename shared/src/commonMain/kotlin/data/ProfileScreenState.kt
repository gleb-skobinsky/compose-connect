package data

import androidx.compose.runtime.Immutable

@Immutable
data class ProfileScreenState(
    val userId: String,
    val photo: String? = null,
    val name: String,
    val status: String = "Active",
    val displayName: String = "",
    val position: String = "",
    val twitter: String = "",
    val timeZone: String? = null,
    val commonChannels: String? = null,
)

