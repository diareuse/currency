package org.billthefarmer.currency.domain.rate

import com.google.common.truth.Truth.assertThat
import org.billthefarmer.currency.domain.model.ExchangeRate
import org.billthefarmer.currency.tooling.MockableTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

class ExchangeRatesErrorDefaultTest : MockableTest() {

    private lateinit var rates: ExchangeRatesErrorDefault

    @Mock
    lateinit var source: ExchangeRates

    @Mock
    lateinit var onError: ExchangeRates

    @Before
    fun prepare() {
        rates = ExchangeRatesErrorDefault(source, onError)
    }

    @Test
    fun `invokes source without error`() {
        val list = emptyList<ExchangeRate>()
        Mockito.`when`(source.getCurrentRates()).thenReturn(list)
        Mockito.`when`(onError.getCurrentRates()).thenThrow(IllegalStateException())
        val result = rates.getCurrentRates()
        assertThat(result).isSameInstanceAs(list)
    }

    @Test
    fun `invokes source with error then onError`() {
        val list = emptyList<ExchangeRate>()
        Mockito.`when`(source.getCurrentRates()).thenThrow(IllegalStateException())
        Mockito.`when`(onError.getCurrentRates()).thenReturn(list)
        val result = rates.getCurrentRates()
        assertThat(result).isSameInstanceAs(list)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `throws when onError fails`() {
        Mockito.`when`(source.getCurrentRates()).thenThrow(IllegalStateException())
        Mockito.`when`(onError.getCurrentRates()).thenThrow(IllegalArgumentException())
        rates.getCurrentRates()
    }

}