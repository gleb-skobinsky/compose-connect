package transport

import data.Message
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import java.util.*

actual fun getTimeNow(): String = Calendar.getInstance().time.toString()

actual val localHost: String = "0.0.0.0"

actual suspend fun webSocketSession(client: HttpClient, path: String, onMessageReceive: (Message) -> Unit): WsSession? {
    val session = CompletableDeferred<DefaultClientWebSocketSession?>()
    CoroutineScope(Dispatchers.Default).launch {
        try {
            client.webSocket(method = HttpMethod.Get, host = localHost, port = 8080, path = path) {
                session.complete(this)
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    onMessageReceive(Json.decodeFromString(Message.serializer(), receivedText))
                }
            }
        } catch (e: java.net.ConnectException) {
            session.complete(null)
            println("Error connecting to websocket at $localHost! Session not initialized.")
        }
    }
    return session.await()
}

actual suspend fun WsSession?.sendMessage(message: Message) {
    (this as DefaultClientWebSocketSession?)?.sendSerialized(message)
}