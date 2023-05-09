package transport

import kotlin.js.Date

actual fun getTimeNow(): String = Date().toTimeString()

actual fun getLocalHost(): String = "127.0.0.1"