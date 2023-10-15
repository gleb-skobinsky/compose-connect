package domain.repository

import data.remote.dto.CreateRoomDto
import data.remote.dto.GetRoomsDto
import domain.model.User

interface RoomRepository {
    suspend fun getRooms(user: User): GetRoomsDto

    suspend fun createRoom(name: String, users: Set<String>, user: User): CreateRoomDto
}