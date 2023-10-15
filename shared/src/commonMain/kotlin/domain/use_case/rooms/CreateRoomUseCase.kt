package domain.use_case.rooms

import common.Resource
import domain.model.ConversationUiState
import domain.model.User
import domain.repository.RoomRepository
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun createRoomUseCase(
    repository: RoomRepository,
    name: String,
    users: Set<String>,
    user: User
): Flow<Resource<Map<String, ConversationUiState>>> = flow {
    try {
        emit(Resource.Loading())
        val result = repository.createRoom(name, users, user).toConvState()
        emit(Resource.Data(mapOf(result.id to result)))
    } catch (e: IOException) {
        emit(Resource.Error(e.message ?: "Couldn't reach server. Check your internet connection."))
    } catch (e: kotlinx.serialization.SerializationException) {
        emit(Resource.Error(e.message ?: "An unexpected error occurred."))
    }
}