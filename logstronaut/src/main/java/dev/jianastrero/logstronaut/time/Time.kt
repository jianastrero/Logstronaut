package dev.jianastrero.logstronaut.time

import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * Returns the current UTC time as a string representation.
 *
 * @return the current UTC time as a string.
 */
fun getCurrentUtcTimeAsString(): String =
    ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT)
