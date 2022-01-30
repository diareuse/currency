package wiki.depasquale.currency.domain.adapter

import androidx.annotation.Keep
import wiki.depasquale.currency.domain.preference.ValueProvider
import java.util.*

class CurrencyValueProvider @Keep constructor() : ValueProvider {

    override fun getDefault() = emptyList<Currency>()

}