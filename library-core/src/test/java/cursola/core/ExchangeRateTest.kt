package cursola.core

import cursola.core.util.todayRange
import org.junit.Test
import java.util.Calendar
import java.util.Date
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
        val rate = ExchangeRate(0.0, date.time)
        assertFalse(rate.isFromToday)
    }

    @Test
    fun isFromToday_returns_negativeWhenTomorrow() {
        val date = Calendar.getInstance()
        date.time = todayRange.endInclusive
        date.add(Calendar.MILLISECOND, 1)
        val rate = ExchangeRate(0.0, date.time)
        assertFalse(rate.isFromToday)
    }

    @Test
    fun constructor_creates_compoundRatio() {
        val usd = ExchangeRate(0.9717)
        val czk = ExchangeRate(24.587)
        val result = ExchangeRate(usd, czk)
        assertEquals(25.303077081403725, result.rate)
    }

    @Test
    fun constructor_creates_compoundRatio_inReverse() {
        val usd = ExchangeRate(0.9717)
        val czk = ExchangeRate(24.587)
        val result = ExchangeRate(czk, usd)
        assertEquals(0.03952088502053931, result.rate)
    }

    @Test
    fun constructor_creates_maxDate_whenFromLarger() {
        val from = ExchangeRate(1.0, Date(1))
        val to = ExchangeRate(1.0, Date(0))
        assertEquals(Date(1), ExchangeRate(from, to).timestamp)
    }

    @Test
    fun constructor_creates_maxDate_whenToLarger() {
        val from = ExchangeRate(1.0, Date(0))
        val to = ExchangeRate(1.0, Date(1))
        assertEquals(Date(1), ExchangeRate(from, to).timestamp)
    }

}