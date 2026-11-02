package cursola.rate.convert

import cursola.rate.ExchangeRateError
import kotlinx.coroutines.test.runTest
import java.util.Date
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CurrencyConverterDefaultTest {

    private val asOf = Date(1_700_000_000_000)
    private val rates = TableConversionRates(mapOf(EUR to 1.0, USD to 1.25, CZK to 25.0))

    private fun converter(vararg favorites: java.util.Currency) =
        CurrencyConverterDefault(rates, ListFavorites(favorites.toList()), FixedTimestamp(asOf))

    @Test
    fun convert_multiplies_amount_by_rate_and_reports_rate_and_timestamp() = runTest {
        val actual = converter().convert(money("10", EUR), USD)

        assertEquals(Conversion(money("10", EUR), money("12.5", USD), 1.25, asOf), actual)
    }

    @Test
    fun convert_keeps_decimal_precision_of_the_amount() = runTest {
        val actual = converter().convert(money("32.3", EUR), USD)

        assertEquals(money("40.375", USD), actual.result)
    }

    @Test
    fun convert_throws_not_found_when_rate_is_missing() = runTest {
        assertFailsWith<ExchangeRateError.NotFoundException> {
            converter().convert(money("1", EUR), GBP)
        }
    }

    @Test
    fun convert_to_favorites_converts_to_each_favorite_in_order() = runTest {
        val actual = converter(CZK, USD).convertToFavorites(money("2", EUR))

        assertEquals(listOf(money("50", CZK), money("2.5", USD)), actual.map { it.result })
    }

    @Test
    fun convert_to_favorites_skips_the_source_currency() = runTest {
        val actual = converter(USD, EUR, CZK).convertToFavorites(money("1", EUR))

        assertEquals(listOf(USD, CZK), actual.map { it.result.currency })
    }

    @Test
    fun convert_to_favorites_skips_favorites_without_a_rate() = runTest {
        val actual = converter(GBP, USD).convertToFavorites(money("1", EUR))

        assertEquals(listOf(USD), actual.map { it.result.currency })
    }

    @Test
    fun conversions_succeed_without_timestamp_when_rates_snapshot_is_unknown() = runTest {
        val converter = CurrencyConverterDefault(rates, ListFavorites(listOf(USD)), RatesTimestamp { null })

        assertEquals(null, converter.convert(money("1", EUR), USD).timestamp)
        assertEquals(listOf(money("1.25", USD)), converter.convertToFavorites(money("1", EUR)).map { it.result })
    }

    @Test
    fun convert_to_favorites_is_empty_without_favorites() = runTest {
        assertEquals(emptyList(), converter().convertToFavorites(money("1", EUR)))
    }

}
