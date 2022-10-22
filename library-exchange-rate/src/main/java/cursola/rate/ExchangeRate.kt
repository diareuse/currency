package cursola.rate

import java.util.Currency
import java.util.Date

data class ExchangeRate(
    val currency: Currency,
    val rate: Double,
    val timestamp: Date
) {

    companion object {

        fun getExchangeRate(from: ExchangeRate, to: ExchangeRate) =
            getExchangeRate(from.rate, to.rate)

        fun getExchangeRate(from: Double, to: Double) =
            1 / from * to

    }

}