package data.remote.dto

typealias GetMessagesDto = List<MessageDto>

fun GetMessagesDto.toConvState() = map {
    it.toMessage()
}