package domain.model

import androidx.compose.runtime.Stable
import data.ProfileScreenState
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
    var accessToken: String = "",
    @SerialName("refresh_token")
    var refreshToken: String = "",
    val image: String? = null
) {
    fun getBearer() = "Bearer $accessToken"

    val fullName = "$firstName $lastName"

    fun toProfileScreen() = ProfileScreenState(
        userId = email,
        name = fullName,
        photo = image
    )
}







