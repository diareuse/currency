package org.billthefarmer.currency.presentation.adapter

import org.billthefarmer.currency.domain.model.ExchangeRate
import org.billthefarmer.currency.presentation.model.CurrencyModel

interface CurrencyModelAdapter {

    fun adapt(rate: ExchangeRate): CurrencyModel

}