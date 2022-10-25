package cursola.rate

import cursola.rate.database.ExchangeRateDatabase
import cursola.rate.network.ExchangeRateService
import cursola.rate.storage.Storage
import org.junit.Before
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

internal abstract class AbstractDataSourceTest {

    internal lateinit var storage: Storage
    protected lateinit var database: ExchangeRateDatabase
    protected lateinit var network: ExchangeRateService

    @Before
    fun prepareInternal() {
        network = mock()
        database = mock()
        storage = mock()
        whenever(database.rates()).thenReturn(mock())
        whenever(database.favorites()).thenReturn(mock())
        mockStorage()
        prepare()
    }

    protected abstract fun prepare()

    // ---

    @Suppress("RedundantUnitExpression")
    private fun mockStorage() {
        val data = mutableMapOf<String, Any>()
        whenever(storage[any()]).then { data.getOrElse(it.getArgument(0)) { "" } }
        whenever(storage.set(any(), any())).then {
            data[it.getArgument(0)] = it.getArgument(1)
            Unit
        }
    }

}