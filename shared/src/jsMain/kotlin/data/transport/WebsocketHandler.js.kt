package data.transport

import data.remote.dto.MessageDto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.w3c.dom.WebSocket

actual class WsHandler : WebSocketHandlerPlatform {

    private val ws = mutableMapOf<String, WebSocket>()
    override suspend fun connectRoom(id: String, onMessageReceive: (MessageDto) -> Unit) {
        if (id !in ws.keys) {
            val connection = WebSocket("${LocalRoute.currentWsUrl}/chat/$id/")
            connection.onopen = {}
            connection.onmessage = { event ->
                try {
                    console.log(event.data)
                    event.data?.let {
                        onMessageReceive(Json.decodeFromString(event.data as String))
                    }
                    console.log(event.data)
                } catch (e: Exception) {
                    console.log("Error: ${e.message}")
                }
            }
            ws[id] = connection
        }
    }

    override suspend fun sendMessage(roomId: String, message: MessageDto) {
        ws[roomId]?.send(Json.encodeToString(message))
    }

    override suspend fun dropOtherConnections(exceptId: String) {
        ws.forEach { (id, session) ->
            if (id != exceptId) {
                println("Closing session $id")
                session.close(
                    code = 1000,
                    reason = "Client is disconnecting all sessions"
                )
            }
        }
        ws.clear()
    }
}