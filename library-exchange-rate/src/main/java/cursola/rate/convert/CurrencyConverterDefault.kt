package cursola.rate.convert

import cursola.rate.ConversionRateDataSource
import cursola.rate.ExchangeRateError
import cursola.rate.FavoriteDataSource
import java.math.BigDecimal
import java.util.Currency
import java.util.Date

class CurrencyConverterDefault(
    private val rates: ConversionRateDataSource,
    private val favorites: FavoriteDataSource,
    private val timestamp: RatesTimestamp
) : CurrencyConverter {

    override suspend fun convert(amount: Money, to: Currency): Conversion {
        val rate = rates.get(amount.currency, to)
        return conversion(amount, to, rate, timestamp.get())
    }

    override suspend fun convertToFavorites(amount: Money): List<Conversion> {
        val rated = favorites.list().filter { it != amount.currency }.mapNotNull { to ->
            try {
                to to rates.get(amount.currency, to)
            } catch (_: ExchangeRateError.NotFoundException) {
                null
            }
        }
        if (rated.isEmpty()) return emptyList()
        val asOf = timestamp.get()
        return rated.map { (to, rate) -> conversion(amount, to, rate, asOf) }
    }

    private fun conversion(amount: Money, to: Currency, rate: Double, asOf: Date?): Conversion {
        val result = Money(amount.amount.multiply(BigDecimal.valueOf(rate)), to)
        return Conversion(amount, result, rate, asOf)
    }

}
