package cursola.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.xml.xml

class HttpClientFactory {

    fun create() = HttpClient(CIO) {
        install(HttpCache)
        install(ContentNegotiation) {
            xml(contentType = ContentType("text", "xml"))
        }
    }

}