package org.billthefarmer.currency.domain.adapter

import org.billthefarmer.currency.domain.preference.ValueProvider
import java.util.*

class CurrencyValueProvider : ValueProvider {

    override fun getDefault() = emptyList<Currency>()

}