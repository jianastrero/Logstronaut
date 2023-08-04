package dev.jianastrero.logstronaut.model

import dev.jianastrero.logstronaut.time.getCurrentUtcTimeAsString

data class LogItem<T>(
    val depth: Int,
    val message: T,
    val time: String = getCurrentUtcTimeAsString()
)

fun LogItem<*>.maxMessageLength(): Int =
    when (message) {
        is Collection<*> -> message.maxOf { it.maxMessageLength() }
        is Array<*> -> message.maxOf { it.maxMessageLength() }
        is Map<*, *> -> message.maxOf { it.maxMessageLength() }
        else -> message.toString().length
    }

private fun <T> T.maxMessageLength(): Int =
    when(this) {
        is Collection<*> -> maxOf { it.maxMessageLength() }
        is Array<*> -> maxOf { it.maxMessageLength() }
        is Map<*, *> -> {
            maxOf(keys.maxOf { it.maxMessageLength() }, values.maxOf { it.maxMessageLength() })
        }
        else -> toString().length
    }
