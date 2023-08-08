package dev.jianastrero.logstronaut.logger

import dev.jianastrero.logstronaut.time.getCurrentUtcTimeAsString

/**
 * The `Loggable` interface represents an object that can be logged.
 *
 * @param T the type of message that can be logged
 */
interface Loggable<T> {
    val depth: Int
    val message: T
    val time: String
}

internal fun <T> loggable(
    depth: Int,
    message: T,
    time: String = getCurrentUtcTimeAsString()
): Loggable<T> = object : Loggable<T> {
    override val depth: Int = depth
    override val message: T = message
    override val time: String = time
}
