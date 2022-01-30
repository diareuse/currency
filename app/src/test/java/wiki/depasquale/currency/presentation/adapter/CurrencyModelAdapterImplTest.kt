package wiki.depasquale.currency.presentation.adapter

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import wiki.depasquale.currency.domain.model.ExchangeRate
import wiki.depasquale.currency.tooling.MockableTest
import java.util.*
import kotlin.random.Random.Default.nextDouble

class CurrencyModelAdapterImplTest : MockableTest() {

    private lateinit var adapter: CurrencyModelAdapterImpl

    override fun prepare() {
        super.prepare()
        adapter = CurrencyModelAdapterImpl()
    }

    @Test
    fun `embeds provided rate`() {
        val rate = ExchangeRate(Currency.getInstance("USD"), nextDouble(), Date())
        val output = adapter.adapt(rate)
        assertThat(output.rate).isSameInstanceAs(rate)
    }

}