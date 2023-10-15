package data.repository

import data.remote.dto.GetMessagesDto
import data.transport.LocalRoute
import data.transport.chirrioClient
import domain.model.User
import domain.repository.MessageRepository
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

object MessageRepositoryImpl : MessageRepository {
    override suspend fun getMessages(room: String, user: User): GetMessagesDto {
        val response = chirrioClient.get("${LocalRoute.currentUrl}/messages/$room/") {
            contentType(ContentType.Application.Json)
            headers.append("Authorization", user.getBearer())
        }
        return Json.decodeFromString(response.bodyAsText())
    }
}