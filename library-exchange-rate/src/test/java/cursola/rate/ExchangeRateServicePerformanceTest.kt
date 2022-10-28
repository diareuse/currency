package cursola.rate

import cursola.rate.analytics.PerformanceService
import cursola.rate.network.ExchangeRateService
import cursola.rate.network.ExchangeRateServicePerformance
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify

class ExchangeRateServicePerformanceTest {

    private lateinit var performance: PerformanceService
    private lateinit var origin: ExchangeRateService
    private lateinit var service: ExchangeRateService

    @Before
    fun prepare() {
        origin = mock()
        performance = spy(Performance())
        service = ExchangeRateServicePerformance(origin, performance, "url", "method")
    }

    @Test
    fun get_tracesNetwork() = runTest {
        service.get()
        verify(performance).network(eq("url"), eq("method"), any())
    }

    @Test
    fun get_calls_origin() = runTest {
        service.get()
        verify(origin).get()
    }

    open class Performance : PerformanceService {
        override suspend fun <T> network(url: String, method: String, body: suspend () -> T): T {
            return body()
        }

        override suspend fun <T> trace(marker: String, body: suspend () -> T): T {
            return body()
        }

        override fun <T> traceInline(marker: String, body: () -> T): T {
            return body()
        }
    }

}