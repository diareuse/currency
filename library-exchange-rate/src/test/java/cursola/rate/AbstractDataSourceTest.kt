package cursola.rate

import cursola.rate.database.ExchangeRateDatabase
import cursola.rate.network.ExchangeRateService
import org.junit.Before
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

internal abstract class AbstractDataSourceTest {

    protected lateinit var database: ExchangeRateDatabase
    protected lateinit var network: ExchangeRateService

    @Before
    fun prepareInternal() {
        network = mock()
        database = mock()
        whenever(database.rates()).thenReturn(mock())
        whenever(database.favorites()).thenReturn(mock())
        prepare()
    }

    protected abstract fun prepare()

}