package wiki.depasquale.currency.domain.preference

import android.content.SharedPreferences

interface SharedPreferenceProvider {

    fun getSharedPreferences(): SharedPreferences

}