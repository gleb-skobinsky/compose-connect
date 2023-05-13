package transport

import data.Message
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import java.util.*

actual fun getTimeNow(): String = Calendar.getInstance().time.toString()

actual val localHost: String = "10.0.2.2"

actual suspend fun webSocketSession(client: HttpClient, path: String, onMessageReceive: (Message) -> Unit): WsSession? {
    var session: DefaultClientWebSocketSession? = null
    withContext(Dispatchers.Default) {
        try {
            client.webSocket(method = HttpMethod.Get, host = localHost, port = 8082, path = path) {
                session = this
                while (true) {
                    this.ensureActive()
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
