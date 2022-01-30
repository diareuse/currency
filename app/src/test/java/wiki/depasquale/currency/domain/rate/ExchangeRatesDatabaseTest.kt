package wiki.depasquale.currency.domain.rate

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mock
import org.mockito.Mockito
import wiki.depasquale.currency.domain.adapter.ExchangeRatesAdapter
import wiki.depasquale.currency.domain.database.DaoRate
import wiki.depasquale.currency.domain.model.ExchangeRate
import wiki.depasquale.currency.domain.model.PersistedRate
import wiki.depasquale.currency.tooling.MockableTest
import java.util.*

class ExchangeRatesDatabaseTest : MockableTest() {

    private lateinit var rates: ExchangeRatesDatabase

    @Mock
    lateinit var dao: DaoRate

    @Mock
    lateinit var adapter: ExchangeRatesAdapter

    @Before
    override fun prepare() {
        super.prepare()
        rates = ExchangeRatesDatabase(dao, adapter) { LongRange.EMPTY }
    }

    @Test(expected = IllegalStateException::class)
    fun `calls source`() {
        Mockito.`when`(dao.getRatesInRange(anyLong(), anyLong()))
            .thenThrow(IllegalStateException())
        rates.getCurrentRates()
    }

    @Test
    fun `returns data from source`() {
        val list = listOf(PersistedRate(Currency.getInstance("USD"), 0.0, Date()))
        Mockito.`when`(dao.getRatesInRange(anyLong(), anyLong())).thenReturn(list)
        for (item in list)
            Mockito.`when`(adapter.adapt(item))
                .thenReturn(ExchangeRate(item.currency, item.rate, item.time))

        val rates = rates.getCurrentRates()

        assertThat(rates).hasSize(list.size)

        for ((index, rate) in rates.withIndex()) {
            val persisted = list[index]
            assertThat(persisted.currency).isEqualTo(rate.currency)
            assertThat(persisted.rate).isEqualTo(rate.rate)
            assertThat(persisted.time).isEqualTo(rate.time)
        }
    }

}