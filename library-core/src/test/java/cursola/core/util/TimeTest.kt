package cursola.core.util

import org.junit.Test
import java.util.Calendar
import kotlin.test.assertEquals

class TimeTest {

    @Test
    fun todayRange_returns_zerothMillisecond() {
        val actual = Calendar.getInstance()
        actual.time = todayRange.start
        val expected = Calendar.getInstance()
        expected.set(Calendar.MILLISECOND, 0)
        expected.set(Calendar.SECOND, 0)
        expected.set(Calendar.MINUTE, 0)
        expected.set(Calendar.HOUR, 0)
        assertEquals(expected, actual)
    }

    @Test
    fun todayRange_returns_lastMillisecond() {
        val actual = Calendar.getInstance()
        actual.time = todayRange.endInclusive
        val expected = Calendar.getInstance()
        expected.set(Calendar.MILLISECOND, 0)
        expected.set(Calendar.SECOND, 0)
        expected.set(Calendar.MINUTE, 0)
        expected.set(Calendar.HOUR, 0)
        expected.add(Calendar.DAY_OF_YEAR, 1)
        expected.add(Calendar.MILLISECOND, -1)
        assertEquals(expected, actual)
    }

}