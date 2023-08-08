package dev.jianastrero.logstronaut.util

internal operator fun String.times(times: Int): String = repeat(times)

internal fun Int.tabs(): String = " " * (4 * this)
