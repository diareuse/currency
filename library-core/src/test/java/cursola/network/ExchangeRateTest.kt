package cursola.network

import cursola.network.util.todayRange
import org.junit.Test
import java.util.Calendar
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ExchangeRateTest {

    @Test
    fun applyTo_returns_correctValue() {
        val rate = ExchangeRate(0.5)
        val result = rate.applyTo(10.0)
        assertEquals(5.0, result)
    }

    @Test
    fun isFromToday_returns_positiveWhenToday() {
        val rate = ExchangeRate(0.0)
        assertTrue(rate.isFromToday)
    }

    @Test
    fun isFromToday_returns_negativeWhenYesterday() {
        val date = Calendar.getInstance()
        date.time = todayRange.start
        date.add(Calendar.MILLISECOND, -1)
        val rate = ExchangeRate(0.0)
        assertFalse(rate.isFromToday)
    }

    @Test
    fun isFromToday_returns_negativeWhenTomorrow() {
        val date = Calendar.getInstance()
        date.time = todayRange.endInclusive
        date.add(Calendar.MILLISECOND, 1)
        val rate = ExchangeRate(0.0)
        assertFalse(rate.isFromToday)
    }

}