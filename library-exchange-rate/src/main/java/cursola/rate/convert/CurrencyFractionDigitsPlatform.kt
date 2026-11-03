package cursola.rate.convert

import java.util.Currency

class CurrencyFractionDigitsPlatform : CurrencyFractionDigits {

    override fun of(currency: Currency) = currency.defaultFractionDigits

}
