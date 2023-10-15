package domain.use_case.users

import common.Resource
import domain.model.User
import domain.repository.UserRepository
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun searchUsersUseCase(
    repository: UserRepository,
    query: String,
    user: User
): Flow<Resource<List<User>>> = flow {
    try {
        emit(Resource.Loading())
        val results = repository.search(query, user)
        emit(Resource.Data(results))
    } catch (e: IOException) {
        emit(Resource.Error("Couldn't reach server. Check your internet connection."))
    } catch (e: kotlinx.serialization.SerializationException) {
        emit(Resource.Error("An unexpected error occurred."))
    }
}