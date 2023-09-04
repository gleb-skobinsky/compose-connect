package data

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val email: String,
    val firstName: String,
    val lastName: String
)