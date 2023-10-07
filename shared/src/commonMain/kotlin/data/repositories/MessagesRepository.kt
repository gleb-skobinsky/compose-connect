package data.repositories

import data.InitialMessages
import data.Resource
import data.User
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import transport.LocalRoute
import transport.chirrioClient

object MessagesRepository {
    suspend fun getMessagesForRoom(roomId: String, currentUser: User): Resource<InitialMessages> {
        val response = chirrioClient.get("${LocalRoute.currentUrl}/messages/$roomId/") {
            contentType(ContentType.Application.Json)
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