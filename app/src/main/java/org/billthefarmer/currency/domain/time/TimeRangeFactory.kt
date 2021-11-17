package org.billthefarmer.currency.domain.time

fun interface TimeRangeFactory {
    fun getTimeRange(): LongRange
}