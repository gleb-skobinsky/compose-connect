package transport

import java.util.*

actual val platformName = "desktop"

actual fun getTimeNow(): String = Calendar.getInstance().time.toString()