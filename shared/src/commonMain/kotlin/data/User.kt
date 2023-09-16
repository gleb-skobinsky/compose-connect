package data

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val email: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null
)

@Serializable
data class CredentialsResponse(
    val refresh: String? = null,
    val access: String? = null
)