package dev.jianastrero.logstronaut

import dev.jianastrero.logstronaut.defaults.LogColorDefaults
import java.util.logging.Level

/**
 * The Logstronaut class provides a simple logging utility for Kotlin projects.
 * It allows you to configure various logging settings and provides log output
 * with customizable formatting and colors.
 */
object Logstronaut {

    internal var paddingStart = 1
    internal var paddingEnd = 10
    internal var level = Level.ALL
    internal var colors: Map<Level, String> = LogColorDefaults.colors()

    /**
     * Configures the given configuration object with the provided parameters.
     *
     * @param config the lambda function that configures the configuration object.
     */
    fun config(config: Config.() -> Unit) {
        Config().apply(config)
    }

    class Config {
        /**
         * Represents the padding start value used for the Logstronaut library.
         *
         * This value determines the amount of padding applied to the start of each logged message.
         *
         * @property paddingStart The padding start value.
         */
        var paddingStart: Int
            get() = Logstronaut.paddingStart
            set(value) {
                Logstronaut.paddingStart = value
            }

        /**
         * The amount of padding applied to the end of the Logstronaut logs.
         *
         * @get Returns the current value of the paddingEnd property, which is obtained from the Logstronaut's paddingEnd property.
         *
         * @set Sets the value of the paddingEnd property in the Logstronaut to the specified value.
         *
         */
        var paddingEnd: Int
            get() = Logstronaut.paddingEnd
            set(value) {
                Logstronaut.paddingEnd = value
            }

        /**
         * Represents the logging level for the Logstronaut library.
         *
         * The logging level determines the verbosity of the logged messages.
         *
         * @property level The current logging level.
         */
        var level: Level
            get() = Logstronaut.level
            set(value) {
                Logstronaut.level = value
            }

        /**
         * Map of colors used for displaying log levels.
         *
         * The keys in the map represent log levels, and the values represent the corresponding color codes used for displaying the log levels.
         */
        var colors: Map<Level, String>
            get() = Logstronaut.colors
            set(value) {
                Logstronaut.colors = value
            }
    }
}
