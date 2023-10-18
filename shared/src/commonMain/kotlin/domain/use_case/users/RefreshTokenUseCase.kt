package domain.use_case.users

import common.Resource
import data.remote.dto.RefreshTokenResponse
import domain.model.User
import domain.repository.UserRepository
import io.ktor.utils.io.errors.IOException

suspend fun refreshTokenUseCase(
    repository: UserRepository,
    user: User
): Resource<RefreshTokenResponse> {
    return try {
        Resource.Data(repository.refreshToken(user))
    } catch (e: IOException) {
        Resource.Error("Couldn't reach server. Check your internet connection.")
    } catch (e: kotlinx.serialization.SerializationException) {
        Resource.Error("An unexpected error occurred.")
    }
}