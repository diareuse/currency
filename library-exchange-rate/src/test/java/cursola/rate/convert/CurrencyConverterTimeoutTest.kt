package cursola.rate.convert

import cursola.rate.ExchangeRateError
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeoutOrNull
import java.util.Date
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.time.Duration.Companion.seconds

class CurrencyConverterTimeoutTest {

    private val delegate = CurrencyConverterDefault(
        TableConversionRates(mapOf(EUR to 1.0, USD to 2.0)),
        ListFavorites(listOf(USD)),
        FixedTimestamp(Date(0))
    )

    @Test
    fun convert_returns_delegate_result_within_timeout() = runTest {
        val actual = CurrencyConverterTimeout(delegate, 1.seconds).convert(money("3", EUR), USD)

        assertEquals(delegate.convert(money("3", EUR), USD), actual)
    }

    @Test
    fun convert_to_favorites_returns_delegate_result_within_timeout() = runTest {
        val actual = CurrencyConverterTimeout(delegate, 1.seconds).convertToFavorites(money("3", EUR))

        assertEquals(delegate.convertToFavorites(money("3", EUR)), actual)
    }

    @Test
    fun convert_throws_not_found_when_delegate_exceeds_timeout() = runTest {
        assertFailsWith<ExchangeRateError.NotFoundException> {
            CurrencyConverterTimeout(HangingConverter(), 1.seconds).convert(money("3", EUR), USD)
        }
    }

    @Test
    fun convert_to_favorites_throws_not_found_when_delegate_exceeds_timeout() = runTest {
        assertFailsWith<ExchangeRateError.NotFoundException> {
            CurrencyConverterTimeout(HangingConverter(), 1.seconds).convertToFavorites(money("3", EUR))
        }
    }

    @Test
    fun outer_timeout_cancels_convert_instead_of_becoming_not_found() = runTest {
        val actual = withTimeoutOrNull(1.seconds) {
            CurrencyConverterTimeout(HangingConverter(), 10.seconds).convert(money("3", EUR), USD)
        }

        assertNull(actual)
    }

    @Test
    fun outer_timeout_cancels_convert_to_favorites_instead_of_becoming_not_found() = runTest {
        val actual = withTimeoutOrNull(1.seconds) {
            CurrencyConverterTimeout(HangingConverter(), 10.seconds).convertToFavorites(money("3", EUR))
        }

        assertNull(actual)
    }

    @Test
    fun convert_propagates_delegate_errors_unchanged() = runTest {
        assertFailsWith<ExchangeRateError.NotFoundException> {
            CurrencyConverterTimeout(delegate, 1.seconds).convert(money("3", EUR), GBP)
        }
    }

}
