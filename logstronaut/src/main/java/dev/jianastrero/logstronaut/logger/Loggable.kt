package dev.jianastrero.logstronaut.logger

import dev.jianastrero.logstronaut.time.getCurrentUtcTimeAsString

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

internal fun Loggable<*>.maxMessageLength(depth: Int): Int =
    when (message) {
        is Collection<*> -> (message as Collection<*>).maxOf { it.maxMessageLength(depth) }
        is Array<*> -> (message as Array<*>).maxOf { it.maxMessageLength(depth) }
        is Map<*, *> -> (message as Map<*, *>).maxOf { it.maxMessageLength(depth) }
        is Loggable<*> -> {
            val x = message as Loggable<*>
            x.maxMessageLength(depth + x.depth)
        }
        else -> message.toString().length + (depth * 4)
    }

private fun <T> T.maxMessageLength(depth: Int): Int =
    when(this) {
        is Collection<*> -> maxOf { it.maxMessageLength(depth) }
        is Array<*> -> maxOf { it.maxMessageLength(depth) }
        is Map<*, *> -> maxOf(keys.maxOf { it.maxMessageLength(depth) }, values.maxOf { it.maxMessageLength(depth) })
        is Loggable<*> -> maxMessageLength(depth + this.depth)
        else -> toString().length + (depth * 4)
    }

internal fun Loggable<*>.stringMessage(depth: Int): List<String> =
    when(message) {
        is Collection<*> -> (message as Collection<*>).flatMap { it.stringMessage(depth) }
        is Array<*> -> (message as Array<*>).flatMap { it.stringMessage(depth) }
        is Map<*, *> -> {
            val x = message as Map<*, *>
            x.keys.flatMap { it.stringMessage(depth) }
                .zip(x.values.flatMap { it.stringMessage(depth) }).map { (key, value) ->
                    "$key: ${value.trim()}"
                }
        }
        is Loggable<*> -> {
            val x = message as Loggable<*>
            stringMessage(depth + this.depth + x.depth)
        }
        else -> listOf("${depth.tabs()}$message")
    }

private fun <T> T.stringMessage(depth: Int): List<String> =
    when(this) {
        is Collection<*> -> flatMap { it.stringMessage(depth) }
        is Array<*> -> flatMap { it.stringMessage(depth) }
        is Map<*, *> -> keys.flatMap { it.stringMessage(depth) }
            .zip(values.flatMap { it.stringMessage(depth) }).map { (key, value) ->
                "$key: ${value.trim()}"
            }
        is Loggable<*> -> stringMessage(depth + this.depth)
        else -> listOf("${depth.tabs()}$this")
    }
