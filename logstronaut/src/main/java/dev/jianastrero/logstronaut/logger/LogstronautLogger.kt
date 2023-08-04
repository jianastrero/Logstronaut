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
        generateStackTrace()
    )
    val length = list.maxOf { it.maxOf { item -> item.message.toString().length + item.depth * 4 } } +
            Logstronaut.paddingStart +
            Logstronaut.paddingEnd

    var log = "╭${"─" * length}╮\n"
    list.forEachIndexed { index, logItems ->
        logItems.forEach { logItem ->
            val tabs = logItem.depth.tabs()
            val remainingSpace = length - logItem.message.toString().length - Logstronaut.paddingStart - tabs.length
            log += "│${" " * Logstronaut.paddingStart}$tabs${logItem.message.toString()}${" " * remainingSpace}│\n"
        }
        if (index != list.lastIndex) {
            log += "├${"─" * length}┤\n"
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

private operator fun String.times(times: Int): String = repeat(times)
private fun Int.tabs(): String = " " * (4 * this)

