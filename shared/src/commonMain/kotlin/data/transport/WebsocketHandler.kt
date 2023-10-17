package data.transport

import data.remote.dto.MessageDto
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.sendSerialized
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.header
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.serialization.json.Json
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.forEach
import kotlin.collections.mutableMapOf
import kotlin.collections.set

interface WebSocketHandlerPlatform {
    suspend fun connectRoom(id: String, onMessageReceive: (MessageDto) -> Unit)

    suspend fun sendMessage(roomId: String, message: MessageDto)

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

    override suspend fun connectRoom(id: String, onMessageReceive: (MessageDto) -> Unit) {
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
                        onMessageReceive(Json.decodeFromString(receivedText))
                    }
                }
            }
        } catch (e: Exception) {
            println("Failed to connect to websocket server:")
            println(e.message)
        }
    }

    override suspend fun sendMessage(roomId: String, message: MessageDto) {
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