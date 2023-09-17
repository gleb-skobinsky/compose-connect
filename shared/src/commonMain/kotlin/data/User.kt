package data

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val email: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val accessToken: String = "",
    val refreshToken: String = "",
) {
    fun getBearer() = "Bearer $accessToken"
}

@Serializable
data class CredentialsResponse(
    val refresh: String = "",
    val access: String = "",
)

@Serializable
data class LogoutRequest(
    val refreshToken: String,
)