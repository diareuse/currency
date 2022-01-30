package wiki.depasquale.currency.presentation.adapter

import wiki.depasquale.currency.domain.model.ExchangeRate
import wiki.depasquale.currency.presentation.model.CurrencyModel

interface CurrencyModelAdapter {

    fun adapt(rate: ExchangeRate): CurrencyModel

}