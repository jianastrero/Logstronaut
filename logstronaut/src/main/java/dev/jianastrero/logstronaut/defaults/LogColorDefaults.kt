package dev.jianastrero.logstronaut.defaults

import java.util.logging.Level

object LogColorDefaults {

    private const val SEVERE = "#b34045"
    private const val WARNING = "#fecf6d"
    private const val INFO = "#4091d7"
    private const val CONFIG = "#2d884d"
    private const val FINE = "#99e9eb"

    fun colors(
        finest: String = FINE,
        finer: String = FINE,
        fine: String = FINE,
        config: String = CONFIG,
        info: String = INFO,
        warning: String = WARNING,
        severe: String = SEVERE,
    ) = mapOf(
        Level.FINEST to finest,
        Level.FINER to finer,
        Level.FINE to fine,
        Level.CONFIG to config,
        Level.INFO to info,
        Level.WARNING to warning,
        Level.SEVERE to severe,
    )

}
