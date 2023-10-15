package domain.model

import androidx.compose.runtime.Stable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class User(
    val email: String,
    @SerialName("first_name")
    val firstName: String = "",
    @SerialName("last_name")
    val lastName: String = "",
    @SerialName("access_token")
    val accessToken: String = "",
    @SerialName("refresh_token")
    val refreshToken: String = "",
) {
    fun getBearer() = "Bearer $accessToken"

    val fullName = "$firstName $lastName"

    companion object {
        val Empty = User(email = "")
    }
}







