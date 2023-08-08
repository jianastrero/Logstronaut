package dev.jianastrero.logstronaut.util

/**
 * Repeats the given string a specified number of times.
 *
 * @param times The number of times to repeat the string.
 * @return The resulting string after being repeated.
 */
internal operator fun String.times(times: Int): String = repeat(times)

/**
 * Returns a string consisting of a number of tabs based on the value of the receiver integer.
 *
 * @receiver The integer value representing the number of tabs.
 * @return A string consisting of the specified number of tabs.
 */
internal fun Int.tabs(): String = " " * (4 * this)
