package transport

import data.Message
import io.ktor.client.*
import kotlinx.serialization.Serializable

expect fun getTimeNow(): String

expect val localHost: String

typealias WsSession = Any

expect suspend fun webSocketSession(client: HttpClient, path: String, onMessageReceive: (Message) -> Unit = {}): WsSession?

expect suspend fun WsSession?.sendMessage(message: Message)

@Serializable
data class WebsocketSend(
    val type: String = "websocket.receive",
    val text: String,
)

@Serializable
data class WebsocketReceive(
    val type: String,
    val text: Message,
)
