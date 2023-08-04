package dev.jianastrero.logstronaut.logger

import android.util.Log
import dev.jianastrero.logstronaut.Logstronaut
import dev.jianastrero.logstronaut.model.LogItem

fun <T> T.logD() {
    val log = generateLog()
    Log.d(null, log)
}

private fun <T> T.generateLog(): String {
    val list = listOf(
        generateTitle(),
        generateStackTrace(),
        generateVariable(0)
    )
    val length = list.maxOf { it.maxOf { item -> item.message.toString().length + item.depth * 4 } } +
            Logstronaut.paddingStart +
            Logstronaut.paddingEnd

    var log = "╭${"─" * length}╮\n"
    list.forEachIndexed { index, logItems ->
        logItems.forEach { logItem ->
            when (logItem.message) {
                is List<*> -> {

                }
                else -> {
                    log += logItem.message.toString().createLine(logItem.depth, length)
                }
            }
        }
        if (index != list.lastIndex) {
            log += length.createSeparator()
        }
    }
    log += "╰${"─" * length}╯"

    return log
}

private fun generateTitle(): List<LogItem<*>> {
    val currentThread = Thread.currentThread()
    val threadName = currentThread.name
    val threadId = currentThread.id
    val title = "Thread: $threadName ($threadId)"
    return listOf(
        LogItem(0, title)
    )
}

private fun generateStackTrace(): List<LogItem<*>> {
    val stackTrace = Thread.currentThread().stackTrace
    val list = mutableListOf<LogItem<*>>()
    var index = 0
    var hasStarted = false

    stackTrace.forEach {
        if (!hasStarted) {
            if (it.className.startsWith("dev.jianastrero.logstronaut")) {
                hasStarted = true
            }

            return@forEach
        }

        if (
            it.className.startsWith("dev.jianastrero.logstronaut") ||
            it.className.matches("(com\\.)?android.*".toRegex()) ||
            it.className.matches("java.*".toRegex()) ||
            it.className.matches("kotlin.*".toRegex())
        ) return@forEach

        list.add(
            LogItem(
                index,
                "${it.className}.${it.methodName}(${it.fileName}:${it.lineNumber})"
            )
        )

        index++
    }
    return list
}

private fun <T> T.generateVariable(depth: Int): List<LogItem<*>> {
    val list = mutableListOf<LogItem<*>>()

    when (this) {
        null -> list.add(LogItem(depth, "null"))
        is Boolean -> list.add(LogItem(depth, this.toString()))
        is Number -> list.add(LogItem(depth, this.toString()))
        is String -> list.add(LogItem(depth, "\"$this\""))
        is Char -> list.add(LogItem(depth, "'$this'"))
        is Array<*> -> list.addAll(generateArray(depth))
        is Collection<*> -> list.addAll(generateCollection(depth))
        else -> list.add(LogItem(depth, generateVariable(depth)))
    }

    return list
}

private fun Collection<*>.generateCollection(depth: Int): List<LogItem<*>> = listOf(LogItem(depth, this))
private fun Array<*>.generateArray(depth: Int) = listOf(LogItem(depth, toList()))

        variables.forEach { variable ->
            list.add(LogItem(variable.depth, "${variable.message}$comma", variable.time))
        }
    }
    list.add(LogItem(depth, "]"))
    return list
}

private fun Array<*>.generateArray(depth: Int) = toList().generateCollection(depth)

private operator fun String.times(times: Int): String = repeat(times)
private fun Int.tabs(): String = " " * (4 * this)
private fun String.createLine(indent: Int, characters: Int): String {
    val remainingSpace = characters - Logstronaut.paddingStart - length - (indent * 4)
    return "│${" " * Logstronaut.paddingStart}${indent.tabs()}$this${" " * remainingSpace}│\n"
}
private fun Int.createSeparator() = "├${"─" * this}┤\n"
