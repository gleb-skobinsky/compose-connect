package transport

import org.w3c.workers.ServiceWorkerGlobalScope
import kotlin.js.Date

actual fun getTimeNow(): String = Date().toTimeString()

external val self: ServiceWorkerGlobalScope