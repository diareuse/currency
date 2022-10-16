package cursola.network

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.jvm.javaio.toInputStream

class RatesServiceImpl(
    private val client: HttpClient,
    private val adapter: ExchangeRatesAdapter,
    private val period: RatePeriod
) : RatesService {

    override suspend fun get(): List<ExchangeRateResponse> {
        return client.get(period.url).bodyAsChannel().toInputStream().use {
            adapter.adapt(it)
        }
    }

}