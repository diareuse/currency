package cursola.network.util

import java.util.Calendar
import java.util.Date

internal val todayRange: ClosedRange<Date>
    get() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.HOUR, 0)
        val start = Date(calendar.timeInMillis)
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        calendar.add(Calendar.MILLISECOND, -1)
        val end = Date(calendar.timeInMillis)
        return start..end
    }