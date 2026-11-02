package cursola.rate.convert

import cursola.rate.ExchangeRate
import cursola.rate.ExchangeRateError
import kotlinx.coroutines.test.runTest
import java.util.Date
import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RatesQueryDefaultTest {

    private val today = Date(1_700_000_000_000)
    private val rates = ListExchangeRates(
        listOf(
            ExchangeRate(USD, 1.25, today),
            ExchangeRate(CZK, 25.0, today),
        )
    )

    @Test
    fun currencies_lists_euro_base_and_every_rated_currency_with_localized_name_and_symbol() = runTest {
        val actual = RatesQueryDefault(rates).currencies(Locale.US)

        assertEquals(
            listOf(
                CurrencyInfo(EUR, "Euro", "€"),
                CurrencyInfo(USD, "US Dollar", "$"),
                CurrencyInfo(CZK, "Czech Koruna", "CZK"),
            ),
            actual
        )
    }

    @Test
    fun currencies_does_not_duplicate_euro_when_source_lists_it() = runTest {
        val source = ListExchangeRates(listOf(ExchangeRate(EUR, 1.0, Date(0)), ExchangeRate(USD, 1.25, today)))

        val actual = RatesQueryDefault(source).currencies(Locale.US).map { it.currency }

        assertEquals(listOf(EUR, USD), actual)
    }

    @Test
    fun currencies_is_empty_when_no_rates_are_known() = runTest {
        assertEquals(emptyList(), RatesQueryDefault(ListExchangeRates(emptyList())).currencies(Locale.US))
    }

    @Test
    fun latest_for_euro_lists_one_euro_in_every_other_currency_at_snapshot_time() = runTest {
        val actual = RatesQueryDefault(rates).latest(EUR)

        assertEquals(
            listOf(
                Conversion(money("1", EUR), money("1.25", USD), 1.25, today),
                Conversion(money("1", EUR), money("25", CZK), 25.0, today),
            ),
            actual
        )
    }

    @Test
    fun latest_for_other_base_rebases_rates_and_includes_euro() = runTest {
        val actual = RatesQueryDefault(rates).latest(USD)

        assertEquals(
            listOf(
                Conversion(money("1", USD), money("0.8", EUR), 0.8, today),
                Conversion(money("1", USD), money("20", CZK), 20.0, today),
            ),
            actual
        )
    }

    @Test
    fun latest_throws_not_found_for_unknown_base() = runTest {
        assertFailsWith<ExchangeRateError.NotFoundException> {
            RatesQueryDefault(rates).latest(GBP)
        }
    }

}
