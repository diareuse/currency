package wiki.depasquale.currency.domain.rate

import wiki.depasquale.currency.domain.adapter.ExchangeRatesAdapter
import wiki.depasquale.currency.domain.model.ExchangeRate
import wiki.depasquale.currency.domain.network.NetworkService
import java.net.URL

class ExchangeRatesNetwork(
    private val url: URL,
    private val service: NetworkService,
    private val adapter: ExchangeRatesAdapter
) : ExchangeRates {

    override fun getCurrentRates(): List<ExchangeRate> {
        val response = service.get(url)
        return adapter.adapt(response)
    }

}