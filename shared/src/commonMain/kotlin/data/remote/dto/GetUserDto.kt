package data.remote.dto

import domain.model.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetUserDto(
    val email: String,
    @SerialName("first_name")
    val firstName: String = "",
    @SerialName("last_name")
    val lastName: String = "",
    val image: String? = null
) {
    fun toUser(): User = User(
        email = email,
        firstName = firstName,
        lastName = lastName,
        image = image
    )
}