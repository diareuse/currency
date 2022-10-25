package cursola.rate.util

import java.util.Calendar
import java.util.Date

val Date.isToday: Boolean
    get() {
        return this in todayRange
    }

val todayRange: ClosedRange<Date>
    get() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.HOUR, 0)
        val start = calendar.time
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        calendar.add(Calendar.MILLISECOND, -1)
        val end = calendar.time
        return start..end
    }
