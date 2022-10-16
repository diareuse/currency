package cursola.network

import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.plugin
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class HttpClientFactoryTest {

    private lateinit var factory: HttpClientFactory

    @Before
    fun prepare() {
        factory = HttpClientFactory()
    }

    @Test
    fun returns_clientWith_cache() {
        factory.create().use {
            it.plugin(HttpCache)
        }
    }

    @Test
    fun returns_clientWith_contentNegotiation() {
        factory.create().use {
            it.plugin(ContentNegotiation)
        }
    }

    @Test
    fun returns_clientWith_cioEngine() {
        factory.create().use {
            val expected = Class.forName("io.ktor.client.engine.cio.CIOEngine")
            assertEquals(expected, it.engine::class.java)
        }
    }

}