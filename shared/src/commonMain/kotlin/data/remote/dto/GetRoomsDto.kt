package data.remote.dto

import data.ChatRoomDto

typealias GetRoomsDto = List<ChatRoomDto>

fun GetRoomsDto.toConvState() = associate {
    it.id to it.toConvState()
}