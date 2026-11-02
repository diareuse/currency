package cursola.rate.convert

import cursola.rate.ExchangeRate
import kotlinx.coroutines.test.runTest
import java.util.Date
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class RatesTimestampExchangeRateTest {

    @Test
    fun timestamp_is_the_newest_rate_date() = runTest {
        val source = ListExchangeRates(
            listOf(
                ExchangeRate(EUR, 1.0, Date(0)),
                ExchangeRate(USD, 1.2, Date(2_000)),
                ExchangeRate(CZK, 25.0, Date(1_000)),
            )
        )

        assertEquals(Date(2_000), RatesTimestampExchangeRate(source).get())
    }

    @Test
    fun timestamp_is_unknown_without_rates() = runTest {
        assertNull(RatesTimestampExchangeRate(ListExchangeRates(emptyList())).get())
    }

}
