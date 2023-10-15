package data.remote.dto

typealias GetRoomsDto = List<ChatRoomDto>

fun GetRoomsDto.toConvState() = associate {
    it.id to it.toConvState()
}