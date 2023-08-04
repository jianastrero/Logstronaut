package dev.jianastrero.logstronaut.model

import dev.jianastrero.logstronaut.time.getCurrentUtcTimeAsString

data class LogItem<T>(
    val message: T,
    val time: String = getCurrentUtcTimeAsString()
)
