package cursola.network

import cursola.core.ExchangeRate
import org.junit.Test
import kotlin.test.assertEquals

class ExchangeRateTest {

    @Test
    fun returns_compoundRatio() {
        val response = makeResponse()
        val result = ExchangeRate(response)
        val expected = ExchangeRate(response.rate, response.timestamp)
        assertEquals(expected, result)
    }

}