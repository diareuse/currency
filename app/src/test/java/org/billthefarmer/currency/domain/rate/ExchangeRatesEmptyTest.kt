package org.billthefarmer.currency.domain.rate

import com.google.common.truth.Truth.assertThat
import org.billthefarmer.currency.tooling.MockableTest
import org.junit.Before
import org.junit.Test

class ExchangeRatesEmptyTest : MockableTest() {

    private lateinit var rates: ExchangeRatesEmpty

    @Before
    override fun prepare() {
        super.prepare()
        rates = ExchangeRatesEmpty()
    }

    @Test
    fun `returns empty list`() {
        assertThat(rates.getCurrentRates()).isEmpty()
    }

}