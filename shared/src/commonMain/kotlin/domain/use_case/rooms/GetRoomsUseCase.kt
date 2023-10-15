package domain.use_case.rooms

import common.Resource
import data.ConversationUiState
import data.remote.dto.toConvState
import domain.model.User
import domain.repository.RoomRepository
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun getRooms(repository: RoomRepository, user: User): Flow<Resource<Map<String, ConversationUiState>>> = flow {
    try {
        emit(Resource.Loading())
        val result = repository.getRooms(user).toConvState()
        emit(Resource.Data(result))
    } catch (e: IOException) {
        emit(Resource.Error(e.message ?: "Couldn't reach server. Check your internet connection."))
    } catch (e: kotlinx.serialization.SerializationException) {
        emit(Resource.Error(e.message ?: "An unexpected error occurred."))
    }
}