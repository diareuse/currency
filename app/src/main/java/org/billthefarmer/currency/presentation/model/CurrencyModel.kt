package org.billthefarmer.currency.presentation.model

import android.content.Context
import org.billthefarmer.currency.domain.model.ExchangeRate

data class CurrencyModel(
    val rate: ExchangeRate
) {

    fun getFlagResource(context: Context): Int {
        val currencyCode = rate.currency.currencyCode
        val resources = context.resources
        val resourceName = "flag_${currencyCode}".lowercase()
        return resources.getIdentifier(resourceName, "drawable", context.packageName)
    }

}