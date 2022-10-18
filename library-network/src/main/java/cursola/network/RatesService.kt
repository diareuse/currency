package cursola.network

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.jvm.javaio.toInputStream
import org.xmlpull.v1.XmlPullParserFactory
import java.text.SimpleDateFormat
import java.util.Locale

interface RatesService {

    suspend fun get(): List<ExchangeRateResponse>

    companion object {

        operator fun invoke(
            period: RatePeriod
        ): RatesService {
            val client = HttpClientFactory().create()
            val parser = XmlPullParserFactory.newInstance().newPullParser()
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            val adapter = ExchangeRatesAdapterImpl(parser, format)
            return RatesServiceImpl(client, adapter, period)
        }

    }

}

private class RatesServiceImpl(
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