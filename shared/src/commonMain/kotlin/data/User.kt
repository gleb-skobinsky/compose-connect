package data

import androidx.compose.runtime.Stable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class User(
    val email: String,
    @SerialName("first_name")
    val firstName: String? = null,
    @SerialName("last_name")
    val lastName: String? = null,
    @SerialName("access_token")
    val accessToken: String = "",
    @SerialName("refresh_token")
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
    val refresh: String,
)

@Serializable
data class SignupRequest(
    val email: String,
    @SerialName("first_name")
    val firstName: String,
    @SerialName("last_name")
    val lastName: String,
    val password: String,
)