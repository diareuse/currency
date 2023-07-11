package cursola.rate.di

import android.content.Context
import cursola.rate.storage.Storage
import cursola.rate.storage.StoragePreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class StorageModule {

    @Provides
    fun storage(
        @ApplicationContext
        context: Context
    ): Storage {
        return StoragePreferences(context)
    }

}