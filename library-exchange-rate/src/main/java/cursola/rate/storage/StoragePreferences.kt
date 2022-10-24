package cursola.rate.storage

import android.content.Context
import androidx.core.content.edit

internal class StoragePreferences(
    context: Context
) : Storage {

    private val preferences = context.getSharedPreferences(
        context.packageName + ".storage", Context.MODE_PRIVATE
    )

    override fun set(name: String, value: String) {
        preferences.edit { putString(name, value) }
    }

    override fun get(name: String): String {
        return preferences.getString(name, null).orEmpty()
    }

}