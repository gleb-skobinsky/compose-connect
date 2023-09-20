package util

import java.util.UUID

actual fun uuid(): String = UUID.randomUUID().toString()