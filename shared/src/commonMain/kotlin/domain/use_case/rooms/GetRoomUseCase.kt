package domain.use_case.rooms

import common.Resource
import data.remote.dto.toConvState
import domain.model.ConversationUiState
import domain.model.User
import domain.repository.MessageRepository
import domain.repository.RoomRepository
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun getRoomUseCase(
    roomRepository: RoomRepository,
    messagesRepository: MessageRepository,
    id: String,
    user: User
): Flow<Resource<ConversationUiState>> = flow {
    try {
        emit(Resource.Loading())
        val result = roomRepository.getRoom(id, user).toConvState()
        val messages = messagesRepository.getMessages(result.id, user).toConvState()
        emit(Resource.Data(result.copy(initialMessages = messages)))
    } catch (e: IOException) {
        emit(Resource.Error(e.message ?: "Couldn't reach server. Check your internet connection."))
    } catch (e: kotlinx.serialization.SerializationException) {
        emit(Resource.Error(e.message ?: "An unexpected error occurred."))
    }
}