package data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenDto(
    val refresh: String
)