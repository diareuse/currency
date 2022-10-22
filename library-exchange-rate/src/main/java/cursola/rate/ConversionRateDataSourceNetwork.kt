package cursola.rate

import cursola.rate.network.ExchangeRateService
import java.util.Currency

internal class ConversionRateDataSourceNetwork(
    private val network: ExchangeRateService
) : ConversionRateDataSource {

    override suspend fun get(from: Currency, to: Currency): Double {
        val rates = network.get()
        var fromRate: ExchangeRate? = null
        var toRate: ExchangeRate? = null
        for (rate in rates) {
            if (rate.currency == from) fromRate = rate
            if (rate.currency == to) toRate = rate
        }

        if (fromRate == null || toRate == null)
            throw ExchangeRateError.NotFoundException("Network doesn't provide conversion $from -> $to")

        return ExchangeRate.getExchangeRate(fromRate, toRate)
    }

}