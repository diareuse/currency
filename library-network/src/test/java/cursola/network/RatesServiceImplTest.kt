@file:OptIn(ExperimentalCoroutinesApi::class)

package cursola.network

import io.ktor.client.HttpClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.io.InputStream
import kotlin.test.assertSame

class RatesServiceImplTest {

    private lateinit var adapter: ExchangeRatesAdapter
    private lateinit var client: HttpClient
    private lateinit var service: RatesServiceImpl

    @Before
    fun prepare() {
        client = HttpClientFactory().create()
        adapter = mock()
        service = RatesServiceImpl(client, adapter, RatePeriod.Daily)
    }

    @After
    fun tearDown() {
        client.close()
    }

    // ---

    @Test
    fun get_calls_adapter() = runTest {
        val argument = argumentCaptor<InputStream>()
        service.get()
        verify(adapter).adapt(argument.capture())
    }

    @Test
    fun get_returns_adaptedList() = runTest {
        val expected = listOf(makeResponse())
        whenever(adapter.adapt(any())).thenReturn(expected)
        val result = service.get()
        assertSame(expected, result)
    }

}