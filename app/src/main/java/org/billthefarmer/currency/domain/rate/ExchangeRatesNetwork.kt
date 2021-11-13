package org.billthefarmer.currency.domain.rate

import org.billthefarmer.currency.domain.adapter.ExchangeRatesAdapter
import org.billthefarmer.currency.domain.model.ExchangeRate
import org.billthefarmer.currency.domain.network.NetworkService
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