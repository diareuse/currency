package wiki.depasquale.currency.domain.rate

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import wiki.depasquale.currency.domain.adapter.ExchangeRatesAdapter
import wiki.depasquale.currency.domain.database.DaoCurrency
import wiki.depasquale.currency.domain.database.DaoRate
import wiki.depasquale.currency.domain.model.ExchangeRate
import wiki.depasquale.currency.domain.model.PersistedCurrency
import wiki.depasquale.currency.domain.model.PersistedRate
import wiki.depasquale.currency.tooling.MockableTest
import java.util.*
import kotlin.random.Random.Default.nextDouble

class ExchangeRatesSavingTest : MockableTest() {

    private lateinit var rates: ExchangeRatesSaving

    @Mock
    lateinit var adapter: ExchangeRatesAdapter

    @Mock
    lateinit var rate: DaoRate

    @Mock
    lateinit var currency: DaoCurrency

    @Mock
    lateinit var source: ExchangeRates

    @Before
    override fun prepare() {
        super.prepare()
        mockAdapter()
        mockSource()
        rates = ExchangeRatesSaving(source, currency, rate, adapter)
    }

    @Test(expected = IllegalStateException::class)
    fun `saves currencies`() {
        Mockito
            .`when`(currency.insert(currencyPersisted))
            .thenThrow(IllegalStateException())

        rates.getCurrentRates()
    }

    @Test(expected = IllegalStateException::class)
    fun `saves rates`() {
        Mockito
            .`when`(currency.insert(currencyPersisted))
            .then {}
        Mockito
            .`when`(rate.insert(ratePersisted))
            .thenThrow(IllegalStateException())

        rates.getCurrentRates()
    }

    @Test
    fun `saves rates after currencies`() {
        var visitedCurrency = false
        var visitedRate = false
        Mockito
            .`when`(currency.insert(currencyPersisted))
            .then {
                visitedCurrency = true
                assertThat(visitedRate).isFalse()
            }
        Mockito
            .`when`(rate.insert(ratePersisted))
            .then {
                visitedRate = true
                assertThat(visitedCurrency).isTrue()
            }

        rates.getCurrentRates()
    }

    // ---

    private val sourceRate = ExchangeRate(
        Currency.getInstance("USD"),
        nextDouble(),
        Date()
    )

    private val currencyPersisted = PersistedCurrency(
        sourceRate.currency
    )

    private val ratePersisted = PersistedRate(
        sourceRate.currency,
        sourceRate.rate,
        sourceRate.time
    )

    private fun mockAdapter(
        source: ExchangeRate = sourceRate,
        currency: PersistedCurrency = currencyPersisted,
        rate: PersistedRate = ratePersisted
    ) {
        Mockito
            .`when`(adapter.adaptCurrency(source))
            .thenReturn(currency)
        Mockito
            .`when`(adapter.adapt(source))
            .thenReturn(rate)
    }

    private fun mockSource() {
        Mockito
            .`when`(source.getCurrentRates())
            .thenReturn(listOf(sourceRate))
    }

}