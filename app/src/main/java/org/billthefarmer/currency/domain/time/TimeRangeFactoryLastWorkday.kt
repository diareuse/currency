package org.billthefarmer.currency.domain.time

import org.billthefarmer.currency.domain.tooling.setToDayEnd
import org.billthefarmer.currency.domain.tooling.setToDayStart
import java.util.*

class TimeRangeFactoryLastWorkday(
    private val calendarProvider: () -> Calendar = { Calendar.getInstance() }
) : TimeRangeFactory {

    override fun getTimeRange(): LongRange {
        val calendar = calendarProvider()
        requireWorkDay(calendar)

        val start = calendar.setToDayStart().timeInMillis
        val end = calendar.setToDayEnd().timeInMillis
        return start..end
    }

    private fun requireWorkDay(calendar: Calendar) {
        val day = calendar[Calendar.DAY_OF_WEEK]
        if (day in Calendar.MONDAY..Calendar.FRIDAY) {
            return
        }
        val weekOffset = when (calendar[Calendar.DAY_OF_WEEK]) {
            // man fuck UK for making sunday first day of the week, that's just wrong
            Calendar.SUNDAY -> -1
            else -> 0
        }
        calendar[Calendar.DAY_OF_WEEK] = Calendar.FRIDAY
        calendar[Calendar.WEEK_OF_YEAR] += weekOffset
    }

}