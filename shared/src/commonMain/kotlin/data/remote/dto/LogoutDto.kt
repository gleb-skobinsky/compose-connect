package data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LogoutDto(
    val refresh: String,
)