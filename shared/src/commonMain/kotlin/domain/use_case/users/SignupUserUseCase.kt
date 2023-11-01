package domain.use_case.users

import common.Resource
import domain.model.User
import domain.repository.UserRepository
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun signupUseCase(
    repository: UserRepository,
    email: String,
    firstName: String,
    lastName: String,
    password: String,
    image: ByteArray?,
    fileExtension: String
): Flow<Resource<User>> = flow {
    try {
        emit(Resource.Loading())
        val user = repository.signup(email, password, firstName, lastName, image, fileExtension)
        emit(Resource.Data(user))
    } catch (e: IOException) {
        emit(Resource.Error(e.message ?: "Couldn't reach server. Check your internet connection."))
    } catch (e: kotlinx.serialization.SerializationException) {
        emit(Resource.Error(e.message ?: "An unexpected error occurred."))
    }
}