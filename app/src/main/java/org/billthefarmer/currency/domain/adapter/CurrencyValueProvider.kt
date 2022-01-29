package org.billthefarmer.currency.domain.adapter

import androidx.annotation.Keep
import org.billthefarmer.currency.domain.preference.ValueProvider
import java.util.*

class CurrencyValueProvider @Keep constructor() : ValueProvider {

    override fun getDefault() = emptyList<Currency>()

}