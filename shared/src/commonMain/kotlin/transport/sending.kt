package transport

import data.Message
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.withContext

expect fun getTimeNow(): String

expect fun getLocalHost(): String

/*
expect fun getPlatformWebsocket(): Any?

expect fun onMessageEnter(message: Message, ws: Any)

 */

suspend fun DefaultClientWebSocketSession.outputMessages() {
    try {
        for (message in incoming) {
            message as? Frame.Text ?: continue
            println(message.readText())
        }
    } catch (e: Exception) {
        println("Error while receiving: " + e.message)
    }
}

suspend fun DefaultClientWebSocketSession.inputMessages() {
    while (true) {
        val message = ""
        if (message.equals("exit", true)) return
        try {
            send(message)
        } catch (e: Exception) {
            println("Error while sending: " + e.message)
            return
        }
    }
}

suspend fun onMessageWs(client: HttpClient, message: Message) {
    client.webSocket(method = HttpMethod.Get, host = "127.0.0.1", port = 8080) {
        sendSerialized(message)
    }
}