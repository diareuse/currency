package org.billthefarmer.currency.domain.preference

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceProviderImpl(
    private val context: Context,
    private val name: String
) : SharedPreferenceProvider {

    override fun getSharedPreferences(): SharedPreferences {
        return context.getSharedPreferences(context.packageName + ".$name", Context.MODE_PRIVATE)
    }

}