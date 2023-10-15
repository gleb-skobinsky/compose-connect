package data.repository

import data.remote.dto.CredentialsDto
import data.remote.dto.GetUserDto
import data.remote.dto.LoginDto
import data.remote.dto.LogoutDto
import data.remote.dto.SearchUserDto
import data.remote.dto.SignupDto
import data.transport.LocalRoute
import data.transport.chirrioClient
import domain.model.User
import domain.repository.UserRepository
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

object UserRepositoryImpl: UserRepository {
    override suspend fun login(email: String, password: String): CredentialsDto {
        val response = chirrioClient.post("${LocalRoute.currentUrl}/users/login/") {
            contentType(ContentType.Application.Json)
            setBody(LoginDto(email, password))
        }
        return Json.decodeFromString(response.bodyAsText())
    }

    override suspend fun getUser(email: String, currentUser: User): GetUserDto {
        val response = chirrioClient.get("${LocalRoute.currentUrl}/users/$email/") {
            contentType(ContentType.Application.Json)
            headers.append("Authorization", currentUser.getBearer())
        }
        return Json.decodeFromString(response.bodyAsText())
    }

    override suspend fun logout(currentUser: User): String? {
        val response = chirrioClient.post("${LocalRoute.currentUrl}/users/logout/") {
            contentType(ContentType.Application.Json)
            setBody(LogoutDto(currentUser.refreshToken))
            headers.append("Authorization", currentUser.getBearer())
        }
        return if (response.status.value == 205) null else "Error: ${response.status.value} - ${response.status.description}"
    }

    override suspend fun search(query: String, currentUser: User): SearchUserDto {
        val response = chirrioClient.get("${LocalRoute.currentUrl}/users/search/$query/") {
            contentType(ContentType.Application.Json)
            headers.append("Authorization", currentUser.getBearer())
        }
        return Json.decodeFromString(response.bodyAsText())
    }

    override suspend fun signup(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): User {
        val request = SignupDto(email, firstName, lastName, password)
        val response = chirrioClient.post("${LocalRoute.currentUrl}/users/signup/") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        return Json.decodeFromString(response.bodyAsText())
    }
}
