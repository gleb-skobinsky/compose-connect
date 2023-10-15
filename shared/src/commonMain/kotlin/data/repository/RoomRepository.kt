package data.repository

import common.util.uuid
import data.remote.dto.CreateRoomDto
import data.remote.dto.GetRoomsDto
import data.transport.LocalRoute
import data.transport.chirrioClient
import domain.model.User
import domain.repository.RoomRepository
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

object RoomRepositoryImpl: RoomRepository {
    override suspend fun getRooms(user: User): GetRoomsDto {
        val response = chirrioClient.get("${LocalRoute.currentUrl}/rooms/${user.email}") {
            contentType(ContentType.Application.Json)
            headers.append("Authorization", user.getBearer())
        }
        return Json.decodeFromString(response.bodyAsText())
    }

    override suspend fun createRoom(name: String, users: Set<String>, user: User): CreateRoomDto {
        val room = CreateRoomDto(id = uuid(), name = name, users = (users + user.email).toList())
        val response = chirrioClient.post("${LocalRoute.currentUrl}/rooms/create/") {
            contentType(ContentType.Application.Json)
            setBody(room)
            headers.append("Authorization", user.getBearer())
        }
        return Json.decodeFromString(response.bodyAsText())
    }
}