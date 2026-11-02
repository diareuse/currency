package wiki.depasquale.currency.platform.rates

import android.net.Uri
import cursola.rate.ExchangeRateError
import cursola.rate.convert.RatesQuery
import wiki.depasquale.cursola.contract.CurrencyContract.Currencies
import java.util.Locale

internal class CurrenciesTable(
    private val rates: RatesQuery,
    private val locale: Locale
) : RatesTable {

    override fun type(uri: Uri) = Currencies.CONTENT_TYPE

    override fun columns(uri: Uri) = Currencies.COLUMNS

    override suspend fun rows(uri: Uri): RatesRows {
        val currencies = try {
            rates.currencies(locale)
        } catch (_: ExchangeRateError.NotFoundException) {
            emptyList()
        }
        return RatesRows(Currencies.COLUMNS, currencies.map { listOf(it.currency.currencyCode, it.displayName, it.symbol) })
    }

}
