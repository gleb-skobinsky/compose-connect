package data.remote.dto

import domain.model.Message

typealias GetMessagesDto = List<MessageDto>

fun GetMessagesDto.toConvState() = map {
    Message(roomId = it.chatRoom, author = it.user, content = it.text, timestamp = it.timestamp)
}