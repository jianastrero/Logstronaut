package dev.jianastrero.logstronaut.time

import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun getCurrentUtcTimeAsString(): String =
    ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT)
