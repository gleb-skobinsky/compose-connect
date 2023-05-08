package transport

import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*

expect fun getTimeNow(): String

expect fun getLocalHost(): String

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