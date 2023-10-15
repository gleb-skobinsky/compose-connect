package domain.repository

import data.remote.dto.GetMessagesDto
import domain.model.User

interface MessageRepository {
    suspend fun getMessages(room: String, user: User): GetMessagesDto
}