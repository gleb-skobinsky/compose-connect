package common.util

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun Instant.toLocal(): LocalDateTime = toLocalDateTime(TimeZone.currentSystemDefault())

operator fun LocalTime.invoke(): String = "${hour.padZero()}:${minute.padZero()}"

fun Int.padZero() = toString().padStart(2, '0')

fun LocalDate.toLabel(): String = "$dayOfMonth ${month.monthName}"

val Month.monthName: String
    get() = when (this) {
        Month.DECEMBER -> "December"
        Month.JANUARY -> "January"
        Month.FEBRUARY -> "February"
        Month.MARCH -> "March"
        Month.APRIL -> "April"
        Month.MAY -> "May"
        Month.JUNE -> "June"
        Month.JULY -> "July"
        Month.AUGUST -> "August"
        Month.SEPTEMBER -> "September"
        Month.OCTOBER -> "October"
        Month.NOVEMBER -> "November"
        else -> ""
    }
