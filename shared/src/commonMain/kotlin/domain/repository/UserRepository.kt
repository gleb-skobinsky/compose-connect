package domain.repository

import domain.model.User
import data.remote.dto.CredentialsDto
import data.remote.dto.GetUserDto
import data.remote.dto.RefreshTokenResponse
import data.remote.dto.SearchUserDto

interface UserRepository {
    suspend fun login(email: String, password: String): CredentialsDto

    suspend fun refreshToken(user: User): RefreshTokenResponse

    suspend fun getUser(email: String, currentUser: User): GetUserDto

    suspend fun listUsers(currentUser: User): SearchUserDto

    suspend fun logout(currentUser: User): String?

    suspend fun search(query: String, currentUser: User): SearchUserDto

    suspend fun signup(email: String, password: String, firstName: String, lastName: String): User
}