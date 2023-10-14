package data.transport

import java.util.*

actual fun getTimeNow(): String = Calendar.getInstance().time.toString()

actual val platformName = "android"

