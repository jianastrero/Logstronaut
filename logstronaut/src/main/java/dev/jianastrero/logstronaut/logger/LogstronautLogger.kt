package dev.jianastrero.logstronaut.logger

import android.util.Log
import dev.jianastrero.logstronaut.Logstronaut
import dev.jianastrero.logstronaut.model.LogItem
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.currentCoroutineContext
import kotlin.coroutines.CoroutineContext

private const val ANSI_RED = "\u001B[31m"

fun <T> T.logD() {
    val log = generateLog()
    Log.d(null, log)
}

suspend fun <T> T.logDSuspended() {
    val coroutineContext = currentCoroutineContext()
    val log = generateLog(coroutineContext)
    Log.d(null, log)
}

private fun <T> T.generateLog(coroutineContext: CoroutineContext? = null): String {
    val list = listOf(
        generateTitle(coroutineContext),
        generateStackTrace()
    )
    val length = list.maxOf { it.maxOf { item -> item.message.toString().length } } +
            Logstronaut.paddingStart +
            Logstronaut.paddingEnd

    var log = "┌${"─" * length}┐\n"
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
    log += "└${"─" * length}┘"

    return log
}

private fun generateTitle(coroutineContext: CoroutineContext?): List<LogItem<*>> {
    val currentThread = Thread.currentThread()
    val threadName = currentThread.name
    val threadId = currentThread.id
    val coroutineName = coroutineContext?.let { coroutineContext[CoroutineName] }?.name
    val title = "Thread: $threadName ($threadId) ${coroutineName?.let { "Coroutine: $it" } ?: ""}"
    return listOf(
        LogItem(0, title)
    )
}

private fun generateStackTrace(): List<LogItem<*>> {
    val stackTrace = Thread.currentThread().stackTrace
    val list = mutableListOf<LogItem<*>>()
    var index = 0

    stackTrace.forEach {
        if (
            it.className.startsWith("java.lang.Thread") ||
            it.className.startsWith("dev.jianastrero.logstronaut") ||
            it.className.startsWith("dalvik.") ||
            it.className.startsWith("java.") ||
            it.className.startsWith("android.") ||
            it.className.startsWith("kotlin.") ||
            it.className.matches("(com\\.)?android.*".toRegex())
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

