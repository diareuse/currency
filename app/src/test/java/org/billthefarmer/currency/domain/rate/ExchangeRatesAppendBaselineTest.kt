package org.billthefarmer.currency.domain.rate

import com.google.common.truth.Truth.assertThat
import org.billthefarmer.currency.domain.model.ExchangeRate
import org.billthefarmer.currency.tooling.MockableTest
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.whenever
import java.util.*
import kotlin.random.Random.Default.nextDouble

class ExchangeRatesAppendBaselineTest : MockableTest() {

    private lateinit var rates: ExchangeRatesAppendBaseline

    @Mock
    lateinit var source: ExchangeRates

    override fun prepare() {
        rates = ExchangeRatesAppendBaseline(source)
    }

    @Test
    fun `keeps source`() {
        val expected = ExchangeRate(Currency.getInstance("USD"), nextDouble(), Date())
        whenever(source.getCurrentRates()).thenReturn(listOf(expected))
        val output = rates.getCurrentRates()
        assertThat(output).contains(expected)
        assertThat(output).hasSize(2)
    }

    @Test
    fun `attaches baseline eur`() {
        whenever(source.getCurrentRates()).thenReturn(emptyList())
        val output = rates.getCurrentRates()
        assertThat(output).hasSize(1)
        assertThat(output[0].currency).isEqualTo(Currency.getInstance("EUR"))
        assertThat(output[0].rate).isEqualTo(1.0)
    }

}