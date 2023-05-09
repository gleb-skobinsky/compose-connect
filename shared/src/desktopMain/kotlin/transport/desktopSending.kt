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

actual val localHost: String = "127.0.0.1"

actual suspend fun webSocketSession(client: HttpClient): WsSession? {
    var session: DefaultClientWebSocketSession? = null
    withContext(Dispatchers.Default) {
        client.webSocket(method = HttpMethod.Get, host = localHost, port = 8082) {
            session = this
            while (true) {
                this.ensureActive()
            }
        }
    }
    return session
}

actual suspend fun WsSession?.sendMessage(message: Message) {
    (this as DefaultClientWebSocketSession?)?.sendSerialized(message)
}