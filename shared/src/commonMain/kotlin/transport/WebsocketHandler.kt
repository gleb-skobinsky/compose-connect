package transport

import data.Message
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.websocket.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

interface WebSocketHandlerPlatform {
    suspend fun connectRoom(id: String, onMessageReceive: (Message) -> Unit)

    suspend fun sendMessage(roomId: String, message: Message)

    suspend fun dropOtherConnections(exceptId: String)
}

expect class WsHandler() : WebSocketHandlerPlatform

open class JvmIosWebsocketHandler : WebSocketHandlerPlatform {
    private val client = HttpClient {
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }
    }

    private val activeSessions = mutableMapOf<String, DefaultClientWebSocketSession>()

    override suspend fun connectRoom(id: String, onMessageReceive: (Message) -> Unit) {
        try {
            if (id !in activeSessions.keys) {
                client.webSocket(
                    method = HttpMethod.Get,
                    host = LocalRoute.current,
                    path = "chat/$id/",
                    request = {
                        header("origin", LocalRoute.currentUrl)
                    }
                ) {
                    activeSessions[id] = this
                    for (frame in incoming) {
                        frame as? Frame.Text ?: continue
                        val receivedText = frame.readText()
                        onMessageReceive(Json.decodeFromString<Message>(receivedText))
                    }
                }
            }
        } catch (e: Exception) {
            println("Failed to connect to websocket server:")
            println(e.message)
        }
    }

    override suspend fun sendMessage(roomId: String, message: Message) {
        activeSessions[roomId]?.sendSerialized(message)
    }

    override suspend fun dropOtherConnections(exceptId: String) {
        activeSessions.forEach { (id, session) ->
            if (id != exceptId) {
                session.close(
                    CloseReason(
                        code = CloseReason.Codes.NORMAL,
                        message = "Client is disconnecting all sessions"
                    )
                )
            }
        }
        activeSessions.clear()
    }
}