package cursola.persistence.di

import android.content.Context
import cursola.persistence.ExchangeRateRepositoryPersistence
import cursola.persistence.Storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
class PersistenceModule {

    @Provides
    fun persistence(
        storage: Storage
    ): ExchangeRateRepositoryPersistence {
        return ExchangeRateRepositoryPersistence(storage)
    }

    @Provides
    fun storage(context: Context): Storage {
        return Storage(context)
    }

}