package common.util

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun Instant.toLocal(): LocalDateTime = toLocalDateTime(TimeZone.currentSystemDefault())

operator fun LocalTime.invoke(): String = "${hour.padZero()}:${minute.padZero()}"

fun Int.padZero() = toString().padStart(2, '0')

fun LocalDate.toLabel(): String = "$dayOfMonth ${month.name}"
