package domain.use_case.messages

import common.Resource
import data.remote.dto.toConvState
import domain.model.Message
import domain.model.User
import domain.repository.MessageRepository
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun getMessagesUseCase(
    repository: MessageRepository,
    room: String,
    user: User
): Flow<Resource<List<Message>>> = flow {
    try {
        emit(Resource.Loading())
        val messages = repository.getMessages(room, user).toConvState()
        emit(Resource.Data(messages))
    } catch (e: IOException) {
        emit(Resource.Error(e.message ?: "Couldn't reach server. Check your internet connection."))
    } catch (e: kotlinx.serialization.SerializationException) {
        emit(Resource.Error(e.message ?: "An unexpected error occurred."))
    }
}