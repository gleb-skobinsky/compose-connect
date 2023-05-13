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

actual suspend fun webSocketSession(client: HttpClient, onMessageReceive: (String) -> Unit): WsSession? {
    val ws = WebSocket("ws://$localHost:8082")
    ws.onopen = {
        ws.send("hello from js client")
    }
    ws.onmessage = { event ->
        try {
            console.log(event.data)
        } catch (e: Exception) {
            console.log("Error: $e.message")
        }
    }
    return ws
}

actual suspend fun WsSession?.sendMessage(message: Message) {
    (this as WebSocket?)?.send(Json.encodeToString(Message.serializer(), message))
}