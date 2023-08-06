package dev.jianastrero.logstronaut.logger

import android.util.Log
import dev.jianastrero.logstronaut.Logstronaut

fun <T> T.logD() {
    val log = generateLog()
    Log.d(null, log)
}

private fun <T> T.generateLog(): String {
    val list = listOf(
        generateTitle(),
        generateStackTrace(),
//        generateVariable(0)
    )
    val length = list.maxOf { it.maxMessageLength(it.depth) } +
            Logstronaut.paddingStart +
            Logstronaut.paddingEnd
    var log: String = length.startLog()

    list.forEachIndexed { index, loggable ->
        loggable.stringMessage(loggable.depth).forEach {
            log += it.createLine(loggable.depth, length)
        }

        if (index != list.lastIndex) {
            log += length.createSeparator()
        }
    }

    log += length.endLog()

    return log
}

private fun Int.startLog() = "╭${"─" * this}╮\n"
private fun Int.endLog() = "╰${"─" * this}╯"

private fun generateTitle(): Loggable<*> {
    val currentThread = Thread.currentThread()
    val threadName = currentThread.name
    val threadId = currentThread.id
    val title = "Thread: $threadName ($threadId)"
    return loggable(0, title)
}

private fun generateStackTrace(): Loggable<*> {
    val stackTrace = Thread.currentThread().stackTrace
    val list = mutableListOf<Loggable<*>>()
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

        list.add(loggable(index, "${it.className}.${it.methodName}(${it.fileName}:${it.lineNumber})"))

        index++
    }
    return loggable(0, list)
}

private fun <T> T.generateVariable(depth: Int): List<Loggable<*>> {
    val list = mutableListOf<Loggable<*>>()

    when (this) {
        null -> list.add(loggable(depth, "null"))
        is Boolean -> list.add(loggable(depth, this.toString()))
        is Number -> list.add(loggable(depth, this.toString()))
        is String -> list.add(loggable(depth, "\"$this\""))
        is Char -> list.add(loggable(depth, "'$this'"))
        is Array<*> -> list.addAll(generateArray(depth))
        is Collection<*> -> list.addAll(generateCollection(depth))
        else -> list.add(loggable(depth, generateVariable(depth)))
    }

    return list
}

private fun Collection<*>.generateCollection(depth: Int): List<Loggable<*>> =
    listOf(loggable(depth, this))
private fun Array<*>.generateArray(depth: Int) = listOf(loggable(depth, toList()))

private operator fun String.times(times: Int): String = repeat(times)
internal fun Int.tabs(): String = " " * (4 * this)
private fun Int.createSeparator() = "├${"─" * this}┤\n"

private fun String.createLine(indent: Int, maxCharacters: Int): String {
    val remainingSpace = maxCharacters - Logstronaut.paddingStart - length - (indent * 4)
    return "│${" " * Logstronaut.paddingStart}$this${" " * remainingSpace}│\n"
}
