package cursola.rate.convert

import cursola.rate.ExchangeRate
import cursola.rate.ExchangeRateError
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeoutOrNull
import java.util.Date
import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.time.Duration.Companion.seconds

class RatesQueryTimeoutTest {

    private val delegate = RatesQueryDefault(ListExchangeRates(listOf(ExchangeRate(USD, 2.0, Date(0)))))

    @Test
    fun currencies_returns_delegate_result_within_timeout() = runTest {
        val actual = RatesQueryTimeout(delegate, 1.seconds).currencies(Locale.US)

        assertEquals(delegate.currencies(Locale.US), actual)
    }

    @Test
    fun latest_returns_delegate_result_within_timeout() = runTest {
        val actual = RatesQueryTimeout(delegate, 1.seconds).latest(EUR)

        assertEquals(delegate.latest(EUR), actual)
    }

    @Test
    fun currencies_throws_not_found_when_delegate_exceeds_timeout() = runTest {
        assertFailsWith<ExchangeRateError.NotFoundException> {
            RatesQueryTimeout(HangingRatesQuery(), 1.seconds).currencies(Locale.US)
        }
    }

    @Test
    fun outer_timeout_cancels_currencies_instead_of_becoming_not_found() = runTest {
        assertNull(withTimeoutOrNull(1.seconds) { RatesQueryTimeout(HangingRatesQuery(), 10.seconds).currencies(Locale.US) })
    }

    @Test
    fun outer_timeout_cancels_latest_instead_of_becoming_not_found() = runTest {
        assertNull(withTimeoutOrNull(1.seconds) { RatesQueryTimeout(HangingRatesQuery(), 10.seconds).latest(EUR) })
    }

    @Test
    fun latest_throws_not_found_when_delegate_exceeds_timeout() = runTest {
        assertFailsWith<ExchangeRateError.NotFoundException> {
            RatesQueryTimeout(HangingRatesQuery(), 1.seconds).latest(EUR)
        }
    }

}
