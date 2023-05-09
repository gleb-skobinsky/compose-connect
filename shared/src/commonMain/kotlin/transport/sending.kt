package transport

import data.Message
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*

expect fun getTimeNow(): String

expect val localHost: String

typealias WsSession = Any

expect suspend fun webSocketSession(client: HttpClient): WsSession?

expect suspend fun WsSession?.sendMessage(message: Message)

