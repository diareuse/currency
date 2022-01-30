package wiki.depasquale.currency.presentation.model

import android.content.Context
import wiki.depasquale.currency.R
import wiki.depasquale.currency.domain.model.ExchangeRate

data class CurrencyModel(
    val rate: ExchangeRate
) {

    fun getFlagResource(context: Context): Int {
        val currencyCode = rate.currency.currencyCode
        val resources = context.resources
        val resourceName = "flag_${currencyCode}".lowercase()
        return resources
            .getIdentifier(resourceName, "drawable", context.packageName)
            .takeUnless { it == 0 } ?: let {
            println("Currency $currencyCode not found")
            R.drawable.flag__unknown
        }
    }

}