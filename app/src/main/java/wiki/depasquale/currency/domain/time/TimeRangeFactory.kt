package wiki.depasquale.currency.domain.time

fun interface TimeRangeFactory {
    fun getTimeRange(): LongRange
}