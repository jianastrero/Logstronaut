package dev.jianastrero.logstronaut

import dev.jianastrero.logstronaut.defaults.LogColorDefaults
import java.util.logging.Level

object Logstronaut {

    internal var paddingStart = 1
    internal var paddingEnd = 10
    internal var level = Level.ALL
    internal var colors: Map<Level, String> = LogColorDefaults.colors()

    fun config(config: Config.() -> Unit) {
        Config().apply(config)
    }

    class Config {
        var paddingStart: Int
            get() = Logstronaut.paddingStart
            set(value) {
                Logstronaut.paddingStart = value
            }

        var paddingEnd: Int
            get() = Logstronaut.paddingEnd
            set(value) {
                Logstronaut.paddingEnd = value
            }

        var level: Level
            get() = Logstronaut.level
            set(value) {
                Logstronaut.level = value
            }

        var colors: Map<Level, String>
            get() = Logstronaut.colors
            set(value) {
                Logstronaut.colors = value
            }
    }
}
