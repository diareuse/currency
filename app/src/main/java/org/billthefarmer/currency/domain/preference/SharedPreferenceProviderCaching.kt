package org.billthefarmer.currency.domain.preference

import android.content.SharedPreferences

class SharedPreferenceProviderCaching(
    private val source: SharedPreferenceProvider
) : SharedPreferenceProvider {

    private var preferences: SharedPreferences? = null

    override fun getSharedPreferences(): SharedPreferences {
        return preferences ?: synchronized(this) {
            preferences ?: source.getSharedPreferences().also {
                preferences = it
            }
        }
    }

}