package domain.use_case.users

import common.Resource
import data.ProfileScreenState
import domain.model.User
import domain.repository.UserRepository
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerializationException

fun listUsers(repository: UserRepository, user: User): Flow<Resource<List<ProfileScreenState>>> = flow {
    try {
        emit(Resource.Loading())
        val users = repository.listUsers(user)
        emit(Resource.Data(users.map { it.toProfileScreen() } ))
    } catch (e: IOException) {
        emit(Resource.Error(e.message ?: "Couldn't reach server. Check your internet connection."))
    } catch (e: SerializationException) {
        emit(Resource.Error(e.message ?: "An unexpected error occurred."))
    }
}