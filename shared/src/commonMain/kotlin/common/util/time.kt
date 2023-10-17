package common.util

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun Instant.toLocal(): LocalDateTime = toLocalDateTime(TimeZone.currentSystemDefault())

fun LocalTime.interpret(): String = "$hour:$minute"
