package transport

import data.Message
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.w3c.dom.WebSocket

actual class WsHandler : WebSocketHandlerPlatform {

    private lateinit var ws: WebSocket
    override suspend fun connectRoom(path: String, onMessageReceive: (Message) -> Unit) {
        ws = WebSocket("${LocalRoute.currentWsUrl}/$path")
        ws.onopen = {}
        ws.onmessage = { event ->
            try {
                console.log(event.data)
                event.data?.let {
                    onMessageReceive(Json.decodeFromString<Message>(event.data as String))
                }
                console.log(event.data)
            } catch (e: Exception) {
                console.log("Error: ${e.message}")
            }
        }
    }

    override suspend fun sendMessage(message: Message) {
        ws.send(Json.encodeToString<Message>(message))
    }
}