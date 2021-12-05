package org.billthefarmer.currency.domain.time

import com.google.common.collect.Range
import com.google.common.truth.Truth.assertThat
import org.billthefarmer.currency.tooling.MockableTest
import org.junit.Test
import java.util.*

class TimeRangeFactoryLastWorkdayTest : MockableTest() {

    @Test
    fun `returns today if workday`() {
        val expected = getCalendarInstance(3, 12, 2021)
        val factory = TimeRangeFactoryLastWorkday { expected }
        val output = factory.getTimeRange()
        assertThat(expected.timeInMillis).isIn(Range.closed(output.first, output.last))
    }

    @Test
    fun `returns friday if saturday`() {
        val expected = getCalendarInstance(3, 12, 2021)
        val factory = TimeRangeFactoryLastWorkday { getCalendarInstance(4, 12, 2021) }
        val output = factory.getTimeRange()
        assertThat(expected.timeInMillis).isIn(Range.closed(output.first, output.last))
    }

    @Test
    fun `returns friday if sunday`() {
        val expected = getCalendarInstance(3, 12, 2021)
        val factory = TimeRangeFactoryLastWorkday { getCalendarInstance(5, 12, 2021) }
        val output = factory.getTimeRange()
        assertThat(expected.timeInMillis).isIn(Range.closed(output.first, output.last))
    }

    @Test
    fun `returns range day start is exactly 0ms`() {
        val factory = TimeRangeFactoryLastWorkday()
        val output = factory.getTimeRange()
        val calendar = Calendar.getInstance().also {
            it.timeInMillis = output.first
        }
        assertThat(calendar[Calendar.DAY_OF_YEAR]).isNotEqualTo(calendar.also { it[Calendar.MILLISECOND]-- }[Calendar.DAY_OF_YEAR])
    }

    @Test
    fun `returns range day end is exactly tomorrow -1ms`() {
        val factory = TimeRangeFactoryLastWorkday()
        val output = factory.getTimeRange()
        val calendar = Calendar.getInstance().also {
            it.timeInMillis = output.last
        }
        assertThat(calendar[Calendar.DAY_OF_YEAR]).isNotEqualTo(calendar.also { it[Calendar.MILLISECOND]++ }[Calendar.DAY_OF_YEAR])
    }

    // ---

    @Suppress("SameParameterValue")
    private fun getCalendarInstance(day: Int, month: Int, year: Int): Calendar {
        return Calendar.getInstance().also {
            it[Calendar.DAY_OF_MONTH] = day
            it[Calendar.MONTH] = month - 1
            it[Calendar.YEAR] = year
        }
    }

}