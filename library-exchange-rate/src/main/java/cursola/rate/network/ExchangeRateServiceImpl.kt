package cursola.rate.network

import cursola.rate.ExchangeRate
import cursola.rate.adapter.ExchangeRatesAdapterImpl
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.jvm.javaio.toInputStream
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.job

class ExchangeRateServiceImpl(
    private val client: HttpClient,
    private val adapter: ExchangeRatesAdapterImpl,
    private val url: String
) : ExchangeRateService {

    override suspend fun get(): List<ExchangeRate> {
        val response = client.get(url).bodyAsChannel()
        return adapter.adapt(response.toInputStream(currentCoroutineContext().job))
    }

}