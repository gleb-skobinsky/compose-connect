package transport

import data.Message
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.websocket.*
import kotlinx.serialization.json.Json

interface WebSocketHandlerPlatform {
    suspend fun connectRoom(path: String, onMessageReceive: (Message) -> Unit)

    suspend fun sendMessage(message: Message)
}

expect class WsHandler() : WebSocketHandlerPlatform

open class JvmIosWebsocketHandler : WebSocketHandlerPlatform {
    private val client = HttpClient {
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }
    }

    private lateinit var ktorWebSocketSession: DefaultClientWebSocketSession

    override suspend fun connectRoom(path: String, onMessageReceive: (Message) -> Unit) {
        client.webSocket(
            method = HttpMethod.Get,
            host = localHost,
            port = 8080,
            path = path,
            request = {
                header("origin", "http://127.0.0.1")
            }
        ) {
            ktorWebSocketSession = this
            for (frame in incoming) {
                frame as? Frame.Text ?: continue
                val receivedText = frame.readText()
                onMessageReceive(Json.decodeFromString(Message.serializer(), receivedText))
            }
        }
    }

    override suspend fun sendMessage(message: Message) {
        ktorWebSocketSession.sendSerialized(message)
    }
}