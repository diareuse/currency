package cursola.rate

import cursola.rate.analytics.AnalyticService
import cursola.rate.analytics.PerformanceService
import cursola.rate.database.ExchangeRateDatabase
import cursola.rate.network.ExchangeRateService
import cursola.rate.storage.Storage
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

internal abstract class AbstractDataSourceTest {

    internal lateinit var storage: Storage
    protected lateinit var database: ExchangeRateDatabase
    protected lateinit var network: ExchangeRateService
    protected lateinit var analytics: AnalyticService
    protected lateinit var performance: PerformanceService

    @Suppress("RedundantUnitExpression")
    @Before
    fun prepareInternal() {
        network = mock()
        database = mock()
        storage = mock()
        analytics = mock()
        whenever(database.rates()).thenReturn(mock())
        whenever(database.favorites()).thenReturn(mock())
        whenever(analytics.log(any(), any())).then { Unit }
        runBlocking {
            whenever(performance.network(any(), any(), any<suspend () -> Any>())).then {
                runBlocking {
                    it.getArgument<suspend () -> Any>(2).invoke()
                }
            }
        }
        runBlocking {
            whenever(performance.trace(any(), any<suspend () -> Any>())).then {
                runBlocking {
                    it.getArgument<suspend () -> Any>(1).invoke()
                }
            }
        }
        whenever(performance.traceInline(any(), any<() -> Any>())).then {
            it.getArgument<() -> Any>(1).invoke()
        }
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