package org.billthefarmer.currency.domain.rate

import com.google.common.truth.Truth.assertThat
import org.billthefarmer.currency.domain.model.ExchangeRate
import org.billthefarmer.currency.domain.model.ExchangeRatePreference
import org.billthefarmer.currency.domain.preference.PreferenceReader
import org.billthefarmer.currency.tooling.MockableTest
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.whenever
import java.util.*

class ExchangeRatesFilterSelectedTest : MockableTest() {

    private lateinit var rates: ExchangeRatesFilterSelected

    @Mock
    lateinit var source: ExchangeRates

    @Mock
    lateinit var preferences: PreferenceReader<ExchangeRatePreference>

    override fun prepare() {
        super.prepare()
        rates = ExchangeRatesFilterSelected(source, preferences)
    }

    @Test
    fun `returns only selected currencies`() {
        val input = listOf(
            ExchangeRate(Currency.getInstance("USD"), 0.0, Date()),
            ExchangeRate(Currency.getInstance("EUR"), 0.0, Date()),
            ExchangeRate(Currency.getInstance("PHP"), 0.0, Date()),
            ExchangeRate(Currency.getInstance("CZK"), 0.0, Date()),
            ExchangeRate(Currency.getInstance("PLN"), 0.0, Date()),
        )
        val preference = ExchangeRatePreference(
            listOf(Currency.getInstance("USD"))
        )
        whenever(source.getCurrentRates()).thenReturn(input)
        whenever(preferences.read()).thenReturn(preference)
        val output = rates.getCurrentRates()

        assertThat(output).hasSize(1)
        assertThat(output[0].currency).isEqualTo(Currency.getInstance("USD"))
    }

}