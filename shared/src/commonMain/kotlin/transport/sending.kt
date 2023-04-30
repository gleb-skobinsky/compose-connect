package transport

import data.Message

expect fun getTimeNow(): String

expect fun getPlatformWebsocket(): Any?

expect fun onMessageEnter(message: Message, ws: Any)