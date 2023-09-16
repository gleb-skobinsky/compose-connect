package transport

import buildVariant.mode
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
        try {
            client.webSocket(
                method = HttpMethod.Get,
                host = Routes[mode][platformName],
                port = mode.toPort(),
                path = path,
                request = {
                    header("origin", "http://${Routes[mode][platformName]}")
                }
            ) {
                ktorWebSocketSession = this
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    onMessageReceive(Json.decodeFromString(Message.serializer(), receivedText))
                }
            }
        } catch (e: Exception) {
            println("Failed to connect to websocket server:")
        }
    }

    override suspend fun sendMessage(message: Message) {
        if (this::ktorWebSocketSession.isInitialized) {
            ktorWebSocketSession.sendSerialized(message)
        }
    }
}