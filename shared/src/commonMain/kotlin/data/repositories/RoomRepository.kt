package data.repositories

import data.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import transport.LocalRoute
import transport.chirrioClient

object RoomRepository {
    suspend fun createRoom(room: ChatRoomCreationDto, currentUser: User): Resource<ChatRoomCreationDto> {
        val response = chirrioClient.post("${LocalRoute.currentUrl}/rooms/create/") {
            contentType(ContentType.Application.Json)
            setBody(room)
            headers.append("Authorization", currentUser.getBearer())
        }
        return try {
            val newRoom = Json.decodeFromString<ChatRoomCreationDto>(response.bodyAsText())
            Resource.Data(newRoom)
        } catch (e: Exception) {
            println("Error: ${response.status}")
            println(e.message)
            Resource.Error("Error creating room", response.status)
        }
    }

    suspend fun getRoomsByUser(user: User): Resource<Map<String, ConversationUiState>> {
        val roomsResponse = chirrioClient.get("${LocalRoute.currentUrl}/rooms/${user.email}") {
            contentType(ContentType.Application.Json)
            headers.append("Authorization", user.getBearer())
        }
        return try {
            val rooms = Json.decodeFromString<UserRooms>(roomsResponse.bodyAsText()).toConvState()
            return Resource.Data(rooms)
        } catch (e: Exception) {
            Resource.Error("Error: ${e.message}", roomsResponse.status)
        }
    }
}