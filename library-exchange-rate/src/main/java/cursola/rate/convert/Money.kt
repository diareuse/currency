package cursola.rate.convert

import java.math.BigDecimal
import java.util.Currency

class Money(amount: BigDecimal, val currency: Currency) {

    val amount: BigDecimal = amount.stripTrailingZeros()

    override fun equals(other: Any?) =
        other is Money && other.amount == amount && other.currency == currency

    override fun hashCode() = 31 * amount.hashCode() + currency.hashCode()

    override fun toString() = "Money(${amount.toPlainString()} ${currency.currencyCode})"

}
