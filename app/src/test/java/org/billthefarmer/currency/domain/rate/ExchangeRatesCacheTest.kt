package org.billthefarmer.currency.domain.rate

import com.google.common.truth.Truth.assertThat
import org.billthefarmer.currency.domain.model.ExchangeRate
import org.billthefarmer.currency.tooling.MockableTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import java.util.*

class ExchangeRatesCacheTest : MockableTest() {

    private lateinit var cache: ExchangeRatesCache

    @Mock
    lateinit var source: ExchangeRates

    @Before
    fun prepare() {
        cache = ExchangeRatesCache(source)
    }

    @Test
    fun `returns identical value`() {
        val list = listOf(ExchangeRate(Currency.getInstance("USD"), 0.0, Date()))
        Mockito.`when`(source.getCurrentRates()).thenReturn(list)
        assertThat(cache.getCurrentRates()).isSameInstanceAs(list)
    }

    @Test
    fun `returns first values on repeated calls`() {
        val list = listOf(ExchangeRate(Currency.getInstance("USD"), 0.0, Date()))
        Mockito.`when`(source.getCurrentRates()).thenReturn(list)
        cache.getCurrentRates()
        Mockito.`when`(source.getCurrentRates()).thenReturn(emptyList())
        assertThat(cache.getCurrentRates()).isSameInstanceAs(list)
    }

}