package org.billthefarmer.currency.presentation.adapter

import com.google.common.truth.Truth.assertThat
import org.billthefarmer.currency.domain.model.ExchangeRate
import org.billthefarmer.currency.tooling.MockableTest
import org.junit.Test
import java.util.*
import kotlin.random.Random.Default.nextDouble

class CurrencyModelAdapterImplTest : MockableTest() {

    private lateinit var adapter: CurrencyModelAdapterImpl

    override fun prepare() {
        adapter = CurrencyModelAdapterImpl()
    }

    @Test
    fun `embeds provided rate`() {
        val rate = ExchangeRate(Currency.getInstance("USD"), nextDouble(), Date())
        val output = adapter.adapt(rate)
        assertThat(output.rate).isSameInstanceAs(rate)
    }

}