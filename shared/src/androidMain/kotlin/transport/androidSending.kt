package transport

import java.util.*

actual fun getTimeNow(): String = Calendar.getInstance().time.toString()

actual fun getLocalHost(): String = "10.0.2.2"
