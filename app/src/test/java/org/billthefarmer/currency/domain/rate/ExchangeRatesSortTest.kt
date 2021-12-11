package org.billthefarmer.currency.domain.rate

import com.google.common.truth.Truth.assertThat
import org.billthefarmer.currency.domain.model.ExchangeRate
import org.billthefarmer.currency.tooling.MockableTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import java.util.*

class ExchangeRatesSortTest : MockableTest() {

    private lateinit var sort: ExchangeRatesSort

    @Mock
    lateinit var source: ExchangeRates

    @Before
    override fun prepare() {
        super.prepare()
        sort = ExchangeRatesSort(source)
    }

    @Test
    fun `returns sorted values`() {
        val date = Date()
        val list = listOf(
            ExchangeRate(Currency.getInstance("USD"), 0.0, date),
            ExchangeRate(Currency.getInstance("CZK"), 0.0, date),
            ExchangeRate(Currency.getInstance("EUR"), 0.0, date),
            ExchangeRate(Currency.getInstance("PHP"), 0.0, date),
        )
        Mockito.`when`(source.getCurrentRates()).thenReturn(list)
        assertThat(sort.getCurrentRates()).isInOrder { o1, o2 ->
            o1 as ExchangeRate
            o2 as ExchangeRate
            o1.currency.displayName.compareTo(o2.currency.displayName)
        }
    }

}