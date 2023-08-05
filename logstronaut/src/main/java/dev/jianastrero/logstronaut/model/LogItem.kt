package dev.jianastrero.logstronaut.model

import dev.jianastrero.logstronaut.logger.tabs
import dev.jianastrero.logstronaut.time.getCurrentUtcTimeAsString

data class LogItem<T>(
    val depth: Int,
    val message: T,
    val time: String = getCurrentUtcTimeAsString()
)

fun LogItem<*>.maxMessageLength(depth: Int): Int =
    when (message) {
        is Collection<*> -> message.maxOf { it.maxMessageLength(depth) }
        is Array<*> -> message.maxOf { it.maxMessageLength(depth) }
        is Map<*, *> -> message.maxOf { it.maxMessageLength(depth) }
        is LogItem<*> -> message.maxMessageLength(depth)
        else -> message.toString().length + (depth * 4)
    }

private fun <T> T.maxMessageLength(depth: Int): Int =
    when(this) {
        is Collection<*> -> maxOf { it.maxMessageLength(depth) }
        is Array<*> -> maxOf { it.maxMessageLength(depth) }
        is Map<*, *> -> maxOf(keys.maxOf { it.maxMessageLength(depth) }, values.maxOf { it.maxMessageLength(depth) })
        is LogItem<*> -> maxMessageLength(depth)
        else -> toString().length + (depth * 4)
    }

fun LogItem<*>.stringMessage(depth: Int): List<String> =
    when(message) {
        is Collection<*> -> message.flatMap { it.stringMessage(depth) }
        is Array<*> -> message.flatMap { it.stringMessage(depth) }
        is Map<*, *> -> message.keys.flatMap { it.stringMessage(depth) }
            .zip(message.values.flatMap { it.stringMessage(depth) }).map { (key, value) ->
                "$key: ${value.trim()}"
            }
        is LogItem<*> -> stringMessage(depth)
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
        is LogItem<*> -> stringMessage(depth)
        else -> listOf("${depth.tabs()}$this")
    }
