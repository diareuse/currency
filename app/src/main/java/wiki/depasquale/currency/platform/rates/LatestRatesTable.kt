package wiki.depasquale.currency.platform.rates

import android.net.Uri
import cursola.rate.ExchangeRateError
import cursola.rate.convert.Conversion
import cursola.rate.convert.RatesQuery
import wiki.depasquale.cursola.contract.CurrencyContract.LatestRates

internal class LatestRatesTable(
    private val rates: RatesQuery
) : RatesTable {

    override fun type(uri: Uri) = LatestRates.CONTENT_TYPE

    override fun columns(uri: Uri) = LatestRates.COLUMNS

    override suspend fun rows(uri: Uri): RatesRows {
        val code = uri.getQueryParameter(LatestRates.PARAM_BASE) ?: LatestRates.DEFAULT_BASE
        val base = currencyOf(code, LatestRates.PARAM_BASE)
        val latest = try {
            rates.latest(base)
        } catch (_: ExchangeRateError.NotFoundException) {
            emptyList()
        }
        return RatesRows(LatestRates.COLUMNS, latest.map(::row))
    }

    private fun row(conversion: Conversion) = listOf(
        conversion.result.currency.currencyCode,
        conversion.rate,
        conversion.timestamp?.time
    )

}
