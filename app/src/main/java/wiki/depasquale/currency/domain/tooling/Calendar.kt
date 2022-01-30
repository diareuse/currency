package wiki.depasquale.currency.domain.tooling

import java.util.*

fun Calendar.setToDayStart() = apply {
    this[Calendar.HOUR_OF_DAY] = 0
    this[Calendar.MINUTE] = 0
    this[Calendar.SECOND] = 0
    this[Calendar.MILLISECOND] = 0
}

fun Calendar.setToDayEnd() = apply {
    setToDayStart()
    this[Calendar.DAY_OF_YEAR]++
    this[Calendar.MILLISECOND]--
}