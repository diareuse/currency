package org.billthefarmer.currency.presentation.adapter

import org.billthefarmer.currency.domain.model.ExchangeRate
import org.billthefarmer.currency.presentation.model.CurrencyModel

class CurrencyModelAdapterImpl : CurrencyModelAdapter {

    override fun adapt(rate: ExchangeRate): CurrencyModel {
        return CurrencyModel(rate)
    }

}