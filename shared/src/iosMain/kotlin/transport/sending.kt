package transport

import data.Message

actual fun getTimeNow(): String = ""

actual fun getPlatformWebsocket(): Any? = null

actual fun onMessageEnter(message: Message, ws: Any) {
}