package data.remote.dto

data class RefreshTokenResponse(
    val refresh: String,
    val access: String
)