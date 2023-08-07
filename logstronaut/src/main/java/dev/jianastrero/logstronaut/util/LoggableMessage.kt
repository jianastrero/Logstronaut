package dev.jianastrero.logstronaut.util

import dev.jianastrero.logstronaut.logger.Loggable
import dev.jianastrero.logstronaut.logger.tabs

internal fun Loggable<*>.stringMessage(depth: Int): List<String> =
    when(message) {
        is Collection<*> -> (message as Collection<*>).message(depth)
        is Array<*> -> (message as Array<*>).message(depth)
        is Map<*, *> -> (message as Map<*, *>).message(depth)
        is Loggable<*> -> (message as Loggable<*>).message(depth + this.depth)
        else -> anyMessage(depth)
    }

private fun <T> T.stringMessage(depth: Int): List<String> =
    when(this) {
        is Collection<*> -> message(depth)
        is Array<*> -> message(depth)
        is Map<*, *> -> message(depth)
        is Loggable<*> -> message(depth)
        else -> anyMessage(depth)
    }

private fun Collection<*>.message(depth: Int): List<String> = flatMap { it.stringMessage(depth) }
private fun Array<*>.message(depth: Int): List<String> = flatMap { it.stringMessage(depth) }
private fun Map<*, *>.message(depth: Int): List<String> =
    keys.flatMap { it.stringMessage(depth) }
        .zip(values.flatMap { it.stringMessage(depth) }).map { (key, value) ->
            "$key: ${value.trim()}"
        }
private fun Loggable<*>.message(depth: Int): List<String> = stringMessage(depth + this.depth)
private fun <T> T.anyMessage(depth: Int): List<String> = listOf("${depth.tabs()}$this")
