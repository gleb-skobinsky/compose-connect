package transport

import java.util.*

actual fun getTimeNow(): String = Calendar.getInstance().time.toString()

actual val localHost: String = "127.0.0.1"

