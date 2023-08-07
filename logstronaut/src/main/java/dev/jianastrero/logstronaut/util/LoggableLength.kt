package dev.jianastrero.logstronaut.util

import dev.jianastrero.logstronaut.logger.Loggable

internal fun Loggable<*>.maxMessageLength(depth: Int): Int =
    when (message) {
        is Collection<*> -> (message as Collection<*>).max(depth)
        is Array<*> -> (message as Array<*>).max(depth)
        is Map<*, *> -> (message as Map<*, *>).max(depth)
        is Loggable<*> -> (message as Loggable<*>).max(depth)
        else -> message.anyMax(depth)
    }

private fun <T> T.maxMessageLength(depth: Int): Int =
    when(this) {
        is Collection<*> -> max(depth)
        is Array<*> -> max(depth)
        is Map<*, *> -> max(depth)
        is Loggable<*> -> max(depth)
        else -> anyMax(depth)
    }

private fun Collection<*>.max(depth: Int): Int = maxOf { it.maxMessageLength(depth) }
private fun Array<*>.max(depth: Int): Int = maxOf { it.maxMessageLength(depth) }
private fun Map<*, *>.max(depth: Int): Int = maxOf { it.maxMessageLength(depth) }
private fun Loggable<*>.max(depth: Int): Int = maxMessageLength(depth + this.depth)
private fun <T> T.anyMax(depth: Int): Int = toString().length + (depth * 4)
