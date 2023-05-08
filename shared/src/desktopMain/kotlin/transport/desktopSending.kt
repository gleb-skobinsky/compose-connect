package transport

import java.util.*

actual fun getTimeNow(): String = Calendar.getInstance().time.toString()

actual fun getLocalHost(): String = "127.0.0.1"

/*
actual fun getPlatformWebsocket(): Any? {
    val client by lazy { OkHttpClient() }
    val request: Request = Request.Builder().url("ws://10.0.2.2:8082/").build()
    val listener = object: WebSocketListener() {
        override fun onMessage(webSocket: WebSocket, text: String) {
            val currentTime: Date = Calendar.getInstance().time
            exampleUiState.addMessage(Message("Web", text, currentTime.toString()))
        }
    }
    return client.newWebSocket(request, listener)
}

actual fun onMessageEnter(message: Message, ws: Any) {
    (ws as WebSocket).send(message.content)
}
 */