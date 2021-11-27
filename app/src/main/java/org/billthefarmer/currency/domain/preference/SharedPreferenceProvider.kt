package org.billthefarmer.currency.domain.preference

import android.content.SharedPreferences

interface SharedPreferenceProvider {

    fun getSharedPreferences(): SharedPreferences

}