package common.util

import java.util.*

actual fun uuid(): String = UUID.randomUUID().toString()