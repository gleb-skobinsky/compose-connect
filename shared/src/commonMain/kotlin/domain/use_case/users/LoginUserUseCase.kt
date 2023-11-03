package domain.use_case.users

import common.Resource
import domain.model.User
import domain.repository.UserRepository
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun loginUseCase(
    repository: UserRepository,
    email: String,
    password: String
): Flow<Resource<User>> = flow {
    try {
        emit(Resource.Loading())
        val tokens = repository.login(email, password)
        val user = User(email = email, accessToken = tokens.access, refreshToken = tokens.refresh)
        val filledUser = repository.getUser(email, user)
        val finalUser = user.copy(
            firstName = filledUser.firstName,
            lastName = filledUser.lastName,
            image = filledUser.image
        )
        emit(Resource.Data(finalUser))
    } catch (e: IOException) {
        emit(Resource.Error("Couldn't reach server. Check your internet connection."))
    } catch (e: kotlinx.serialization.SerializationException) {
        emit(Resource.Error("An unexpected error occurred. Check your email and password."))
    }
}
