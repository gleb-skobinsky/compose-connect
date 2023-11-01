package data.repository

import common.util.uuid
import data.remote.dto.CredentialsDto
import data.remote.dto.GetUserDto
import data.remote.dto.LoginDto
import data.remote.dto.LogoutDto
import data.remote.dto.RefreshTokenDto
import data.remote.dto.RefreshTokenResponse
import data.remote.dto.SearchUserDto
import data.remote.dto.SignupDto
import data.transport.LocalRoute
import data.transport.chirrioClient
import domain.model.User
import domain.repository.UserRepository
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.serialization.json.Json

object RemoteUserRepository: UserRepository {
    override suspend fun login(email: String, password: String): CredentialsDto {
        val response = chirrioClient.post("${LocalRoute.currentUrl}/users/login/") {
            contentType(ContentType.Application.Json)
            setBody(LoginDto(email, password))
        }
        return Json.decodeFromString(response.bodyAsText())
    }

    override suspend fun refreshToken(user: User): RefreshTokenResponse {
        val response = chirrioClient.post("${LocalRoute.currentUrl}/users/refresh-token/") {
            contentType(ContentType.Application.Json)
            setBody(RefreshTokenDto(refresh = user.refreshToken))
            headers.append("Authorization", user.getBearer())
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

    override suspend fun listUsers(currentUser: User): SearchUserDto {
        val response = chirrioClient.get("${LocalRoute.currentUrl}/users/list/") {
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
        lastName: String,
        image: ByteArray?,
        imageExtension: String
    ): User {
        val request = SignupDto(email, firstName, lastName, password)
        val response = chirrioClient.post("${LocalRoute.currentUrl}/users/signup/") {
            contentType(ContentType.Application.Json)
            setBody(MultiPartFormDataContent(
                formData {
                    append("email", request.email)
                    append("first_name", request.firstName)
                    append("last_name", request.lastName)
                    append("password", request.password)
                    image?.let {
                        append("image", it, Headers.build {
                            append(HttpHeaders.ContentType, "image/$imageExtension")
                            append(HttpHeaders.ContentDisposition, "filename=\"${uuid()}.$imageExtension\"")
                        })
                    }
                },
                boundary = "WebAppBoundary"
            )
            )
        }
        return Json.decodeFromString(response.bodyAsText())
    }
}

