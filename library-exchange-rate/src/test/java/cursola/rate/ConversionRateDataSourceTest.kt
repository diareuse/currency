package cursola.rate

import cursola.rate.database.ExchangeRateStored
import cursola.rate.di.ExchangeRateModule
import cursola.rate.model.makeExchangeRate
import kotlinx.coroutines.test.runTest
import org.mockito.kotlin.inOrder
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.io.IOException
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ConversionRateDataSourceTest : AbstractDataSourceTest() {

    private lateinit var source: ConversionRateDataSource

    override fun prepare() {
        source = ExchangeRateModule.getInstance().conversionRate(network, database)
    }

    // ---

    @Test
    fun get_returns_fromNetwork() = runTest {
        val subject = prepareTest()
        whenever(network.get()).thenReturn(subject.asList())
        val result = source.get(subject.fromCurrency, subject.toCurrency)
        assertEquals(1 / subject.fromRate * subject.toRate, result)
    }

    @Test
    fun get_returns_fromDatabase() = runTest {
        val subject = prepareTest()
        val rates = database.rates()
        whenever(network.get()).thenReturn(emptyList())
        whenever(rates.get(subject.fromCode)).thenReturn(subject.fromRate)
        whenever(rates.get(subject.toCode)).thenReturn(subject.toRate)
        val result = source.get(subject.fromCurrency, subject.toCurrency)
        assertEquals(1 / subject.fromRate * subject.toRate, result)
    }

    @Test(expected = ExchangeRateError.NotFoundException::class)
    fun get_throws_whenUnavailableData() = runTest {
        val subject = prepareTest()
        val rates = database.rates()
        whenever(rates.get(subject.fromCode)).thenReturn(null)
        whenever(rates.get(subject.toCode)).thenReturn(null)
        whenever(network.get()).thenThrow(IOException())
        source.get(subject.fromCurrency, subject.toCurrency)
    }

    @Test
    fun get_saves_networkDataToDatabase() = runTest {
        val subject = prepareTest()
        val rates = database.rates()
        whenever(network.get()).thenReturn(listOf(subject.from, subject.to))
        source.get(subject.fromCurrency, subject.toCurrency)
        rates.inOrder {
            verify().insert(ExchangeRateStored(subject.from))
            verify().insert(ExchangeRateStored(subject.to))
        }
    }

    @Test
    fun get_cachesConversion() = runTest {
        val subject = prepareTest()
        whenever(network.get()).thenReturn(listOf(subject.from, subject.to))
        source.get(subject.fromCurrency, subject.toCurrency)
        source.get(subject.fromCurrency, subject.toCurrency)
        verify(network, times(1)).get()
    }

    // ---

    private fun prepareTest(): TestSubject {
        while (true) {
            return TestSubject().takeUnless { it.toCode == it.fromCode } ?: continue
        }
    }

    data class TestSubject(
        val from: ExchangeRate = makeExchangeRate(),
        val to: ExchangeRate = makeExchangeRate()
    ) {

        val fromCurrency get() = from.currency
        val toCurrency get() = to.currency

        val fromCode get() = fromCurrency.currencyCode!!
        val toCode get() = toCurrency.currencyCode!!

        val fromRate get() = from.rate
        val toRate get() = to.rate

        fun asList() = listOf(from, to)

    }

}