package cursola.rate.adapter

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals

class ExchangeRatesAdapterTest {

    private val adapter = ExchangeRatesAdapter()

    @Test
    fun concurrent_adapts_each_return_rates_of_their_own_stream() = runBlocking {
        val first = StalledStream(PREFIX, cubes("USD"))
        val pending = async(Dispatchers.Default) { adapter.adapt(first) }
        withContext(Dispatchers.IO) { first.awaitStalled() }

        val second = adapter.adapt(ByteArrayInputStream((PREFIX + cubes("CZK")).toByteArray()))
        first.release()

        assertEquals(listOf("CZK"), second.map { it.currency.currencyCode })
        assertEquals(listOf("USD"), pending.await().map { it.currency.currencyCode })
    }

    private fun cubes(code: String) =
        "<Cube time=\"2024-01-02\"><Cube currency=\"$code\" rate=\"1.5\"/></Cube></Cube></gesmes:Envelope>"

    private class StalledStream(head: String, tail: String) : InputStream() {

        private val head = ByteArrayInputStream(head.toByteArray())
        private val tail = ByteArrayInputStream(tail.toByteArray())
        private val stalled = CountDownLatch(1)
        private val released = CountDownLatch(1)

        fun awaitStalled() = check(stalled.await(TIMEOUT_SECONDS, TimeUnit.SECONDS))

        fun release() = released.countDown()

        override fun read(): Int {
            val buffer = ByteArray(1)
            return if (read(buffer, 0, 1) < 0) -1 else buffer[0].toInt() and 0xFF
        }

        override fun read(b: ByteArray, off: Int, len: Int): Int {
            if (head.available() > 0) return head.read(b, off, len)
            stalled.countDown()
            check(released.await(TIMEOUT_SECONDS, TimeUnit.SECONDS))
            return tail.read(b, off, len)
        }

    }

    private companion object {
        const val TIMEOUT_SECONDS = 5L
        const val PREFIX =
            "<gesmes:Envelope xmlns:gesmes=\"http://www.gesmes.org/xml/2002-08-01\" " +
                "xmlns=\"http://www.ecb.int/vocabulary/2002-08-01/eurofxref\"><Cube>"
    }

}
