package cursola.rate.di

import android.content.Context
import cursola.rate.storage.Storage
import cursola.rate.storage.StoragePreferences
import spike.Include

@Include
internal fun storage(
    context: Context
): Storage {
    return StoragePreferences(context)
}
