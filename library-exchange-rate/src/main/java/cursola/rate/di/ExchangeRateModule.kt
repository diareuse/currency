package cursola.rate.di

import cursola.rate.ConversionRateDataSource
import cursola.rate.ConversionRateDataSourceDatabase
import cursola.rate.ConversionRateDataSourceErrorReducer
import cursola.rate.ConversionRateDataSourceNetwork
import cursola.rate.ExchangeRateDataSource
import cursola.rate.ExchangeRateDataSourceDatabase
import cursola.rate.ExchangeRateDataSourceErrorReducer
import cursola.rate.ExchangeRateDataSourceNetwork
import cursola.rate.ExchangeRateDataSourceSort
import cursola.rate.FavoriteDataSource
import cursola.rate.FavoriteDataSourceDatabase
import cursola.rate.LatestValueDataSource
import cursola.rate.LatestValueDataSourceDefault
import cursola.rate.LatestValueDataSourceImpl
import cursola.rate.database.ExchangeRateDatabase
import cursola.rate.network.ExchangeRateService
import cursola.rate.network.ExchangeRateServiceBaseline
import cursola.rate.network.ExchangeRateServiceCaching
import cursola.rate.network.ExchangeRateServicePeg
import cursola.rate.network.ExchangeRateServiceSaving
import cursola.rate.storage.Storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import org.jetbrains.annotations.TestOnly

@Module
@InstallIn(ActivityRetainedComponent::class)
internal class ExchangeRateModule {

    @Provides
    fun conversionRate(
        network: ExchangeRateService,
        database: ExchangeRateDatabase
    ): ConversionRateDataSource {
        return ConversionRateDataSourceErrorReducer(
            ConversionRateDataSourceNetwork(wrap(network, database)),
            ConversionRateDataSourceDatabase(database)
        )
    }

    @Provides
    fun exchangeRate(
        network: ExchangeRateService,
        database: ExchangeRateDatabase
    ): ExchangeRateDataSource {
        var source: ExchangeRateDataSource
        source = ExchangeRateDataSourceErrorReducer(
            ExchangeRateDataSourceNetwork(wrap(network, database)),
            ExchangeRateDataSourceDatabase(database)
        )
        source = ExchangeRateDataSourceSort(source)
        return source
    }

    @Provides
    fun favorites(
        database: ExchangeRateDatabase
    ): FavoriteDataSource {
        return FavoriteDataSourceDatabase(database)
    }

    @Provides
    fun latestValue(
        storage: Storage
    ): LatestValueDataSource {
        var source: LatestValueDataSource
        source = LatestValueDataSourceImpl(storage)
        source = LatestValueDataSourceDefault(source)
        return source
    }

    // ---

    private fun wrap(
        network: ExchangeRateService,
        database: ExchangeRateDatabase
    ): ExchangeRateService {
        var service = network
        service = ExchangeRateServicePeg(service)
        service = ExchangeRateServiceSaving(service, database)
        service = ExchangeRateServiceBaseline(service)
        service = ExchangeRateServiceCaching(service)
        return service
    }

    companion object {

        @TestOnly
        internal fun getInstance() = ExchangeRateModule()

    }

}