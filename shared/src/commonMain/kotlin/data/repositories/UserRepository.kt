package data.repositories

import data.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import transport.LocalRoute
import transport.chirrioClient

object UserRepository {
    suspend fun login(
        email: String,
        password: String,
    ): Resource<User> {
        try {
            val response = chirrioClient.post("${LocalRoute.currentUrl}/token/") {
                contentType(ContentType.Application.Json)
                setBody(LoginForm(email, password))
            }
            return if (response.status.value in 200..299) {
                val credentials = Json.decodeFromString<CredentialsResponse>(response.bodyAsText())
                var user = User(email = email, accessToken = credentials.access, refreshToken = credentials.refresh)
                val userResponse = chirrioClient.post("${LocalRoute.currentUrl}/user/") {
                    contentType(ContentType.Application.Json)
                    setBody(user)
                    headers.append("Authorization", user.getBearer())
                }
                user = Json.decodeFromString<User>(userResponse.bodyAsText())
                Resource.Data(user)
            } else {
                Resource.Error(
                    message = "Error during login. Check your email and password.",
                    status = response.status
                )
            }
        } catch (e: Exception) {
            println(e.stackTraceToString())
            return Resource.Error(
                message = "Failed to connect to server. Check your Internet connection and try again.",
                status = HttpStatusCode.ServiceUnavailable
            )
        }
    }

    suspend fun logout(user: User): Resource<User?> {
        try {
            val response = chirrioClient.post("${LocalRoute.currentUrl}/logout/") {
                contentType(ContentType.Application.Json)
                setBody(LogoutRequest(user.refreshToken))
                headers.append("Authorization", user.getBearer())
            }
            return if (response.status.value in 200..299) {
                Resource.Data(null)
            } else {
                Resource.Error(
                    message = "Something went wrong on our side. Still logging out.",
                    status = response.status
                )
            }
        } catch (e: Exception) {
            return Resource.Error(
                message = "Couldn't reach server. Still logging out.",
                status = HttpStatusCode.ServiceUnavailable
            )
        }
    }

    suspend fun signupUser(request: SignupRequest): Resource<User> {
        try {
            val response = chirrioClient.post("${LocalRoute.currentUrl}/signup/") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            return if (response.status.value in 200..299) {
                val user = Json.decodeFromString<User>(response.bodyAsText())
                Resource.Data(user)
            } else {
                Resource.Error(
                    message = "Error during signup. Check your email and password.",
                    status = response.status
                )
            }
        } catch (e: Exception) {
            return Resource.Error(
                message = "Couldn't reach server.",
                status = HttpStatusCode.ServiceUnavailable
            )
        }
    }
}

