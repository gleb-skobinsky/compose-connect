package data.repositories

import data.InitialMessages
import data.Resource
import data.User
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import transport.LocalRoute
import transport.chirrioClient

object MessagesRepository {
    suspend fun getInitialMessagesForRoom(roomId: String, currentUser: User): Resource<InitialMessages> {
        val response = chirrioClient.post("${LocalRoute.currentUrl}/messages-by-room/") {
            contentType(ContentType.Application.Json)
            setBody(InitialMessagesRequest(roomId))
            headers.append("Authorization", currentUser.getBearer())
        }
        return try {
            val messages = Json.decodeFromString<InitialMessages>(response.bodyAsText())
            Resource.Data(messages)
        } catch (e: Exception) {
            println("Error")
            println(e.message)
            Resource.Error("Error fetching messages", response.status)
        }
    }
}

@Serializable
data class InitialMessagesRequest(
    @SerialName("room_id")
    val roomId: String,
)