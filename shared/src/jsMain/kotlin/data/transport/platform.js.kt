package data.transport

import kotlin.js.Date

actual val platformName = "js"

actual fun getTimeNow(): String = Date().toTimeString()