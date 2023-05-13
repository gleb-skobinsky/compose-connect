package transport

import data.Message
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

actual fun getTimeNow(): String = Calendar.getInstance().time.toString()

actual val localHost: String = "0.0.0.0"

actual suspend fun webSocketSession(client: HttpClient, path: String, onMessageReceive: (String) -> Unit): WsSession? {
    var session: DefaultClientWebSocketSession? = null
    withContext(Dispatchers.Default) {
        try {
            client.webSocket(method = HttpMethod.Get, host = localHost, port = 8080, path = path) {
                session = this
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    onMessageReceive(receivedText)
                }
            }
        } catch (e: java.net.ConnectException) {
            println("Error connecting to websocket at $localHost! Session not initialized.")
        }
    }
    return session
}

actual suspend fun WsSession?.sendMessage(message: Message) {
    (this as DefaultClientWebSocketSession?)?.sendSerialized(message)
}