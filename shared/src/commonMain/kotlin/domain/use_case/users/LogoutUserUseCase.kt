package domain.use_case.users

import domain.model.User
import domain.repository.UserRepository
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun logoutUseCase(repository: UserRepository, user: User): Flow<String?> = flow {
    try {
        emit(repository.logout(user))
    } catch (e: IOException) {
        emit( "Couldn't reach server. Check your internet connection.")
    } catch (e: kotlinx.serialization.SerializationException) {
        emit( "An unexpected error occurred.")
    }
}