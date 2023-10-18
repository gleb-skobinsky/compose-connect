package data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenResponse(
    val refresh: String,
    val access: String
)