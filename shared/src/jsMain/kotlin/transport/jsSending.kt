package transport

import data.Message
import io.ktor.client.*
import kotlinx.serialization.json.Json
import org.w3c.dom.WebSocket
import org.w3c.workers.ServiceWorkerGlobalScope
import kotlin.js.Date

actual fun getTimeNow(): String = Date().toTimeString()

external val self: ServiceWorkerGlobalScope

actual val localHost: String = "127.0.0.1"

actual suspend fun webSocketSession(client: HttpClient): WsSession? {
    return WebSocket("ws://$localHost:8082")
}

actual suspend fun WsSession?.sendMessage(message: Message) {
    (this as WebSocket?)?.send(Json.encodeToString(Message.serializer(), message))
}