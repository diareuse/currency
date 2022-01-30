package wiki.depasquale.currency.presentation.adapter

import wiki.depasquale.currency.domain.model.ExchangeRate
import wiki.depasquale.currency.presentation.model.CurrencyModel

class CurrencyModelAdapterImpl : CurrencyModelAdapter {

    override fun adapt(rate: ExchangeRate): CurrencyModel {
        return CurrencyModel(rate)
    }

}