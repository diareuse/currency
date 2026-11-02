package wiki.depasquale.currency.platform.rates

import android.net.Uri
import cursola.rate.ExchangeRateError
import cursola.rate.convert.Conversion
import cursola.rate.convert.CurrencyConverter
import cursola.rate.convert.Money
import wiki.depasquale.cursola.contract.CurrencyContract.Conversions
import java.util.Currency

internal class ConversionsTable(
    private val converter: CurrencyConverter
) : RatesTable {

    override fun type(uri: Uri) = Conversions.CONTENT_TYPE

    override fun columns(uri: Uri) = Conversions.COLUMNS

    override suspend fun rows(uri: Uri): RatesRows {
        val amount = amountOf(uri.getQueryParameter(Conversions.PARAM_AMOUNT), Conversions.PARAM_AMOUNT)
        val from = currencyOf(uri.getQueryParameter(Conversions.PARAM_FROM), Conversions.PARAM_FROM)
        val codes = uri.getQueryParameters(Conversions.PARAM_TO)
        require(codes.isNotEmpty()) { "Missing ${Conversions.PARAM_TO}" }
        require(codes.size <= MAX_TARGETS) { "At most $MAX_TARGETS ${Conversions.PARAM_TO} are supported" }
        val targets = codes.map { currencyOf(it, Conversions.PARAM_TO) }.distinct()
        return RatesRows(Conversions.COLUMNS, convert(Money(amount, from), targets).map(::row))
    }

    private suspend fun convert(source: Money, targets: List<Currency>) = targets.mapNotNull { to ->
        try {
            converter.convert(source, to)
        } catch (_: ExchangeRateError.NotFoundException) {
            null
        }
    }

    private fun row(conversion: Conversion) = listOf(
        conversion.source.currency.currencyCode,
        conversion.result.currency.currencyCode,
        conversion.source.amount.toDouble(),
        conversion.result.amount.toDouble(),
        conversion.rate,
        conversion.timestamp?.time
    )

    private companion object {
        const val MAX_TARGETS = 64
    }

}
