package data.repositories

import data.ChatRoomCreationDto
import data.Resource
import data.User
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import transport.LocalRoute
import transport.chirrioClient

object RoomRepository {
    suspend fun createRoom(room: ChatRoomCreationDto, currentUser: User): Resource<ChatRoomCreationDto> {
        val response = chirrioClient.post("${LocalRoute.currentUrl}/new-room/") {
            contentType(ContentType.Application.Json)
            setBody(room)
            headers.append("Authorization", currentUser.getBearer())
        }
        return try {
            val newRoom = Json.decodeFromString(ChatRoomCreationDto.serializer(), response.bodyAsText())
            Resource.Data(newRoom)
        } catch (e: Exception) {
            println("Error")
            println(e.message)
            Resource.Error("Error creating room", response.status)
        }
    }
}