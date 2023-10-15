package data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CredentialsDto(
    val refresh: String = "",
    val access: String = "",
)