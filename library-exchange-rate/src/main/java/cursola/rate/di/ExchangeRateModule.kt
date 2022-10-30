package cursola.rate.di

import cursola.rate.ConversionRateDataSource
import cursola.rate.ConversionRateDataSourceDatabase
import cursola.rate.ConversionRateDataSourceDatabaseToday
import cursola.rate.ConversionRateDataSourceErrorReducer
import cursola.rate.ConversionRateDataSourceNetwork
import cursola.rate.ExchangeRateDataSource
import cursola.rate.ExchangeRateDataSourceDatabase
import cursola.rate.ExchangeRateDataSourceErrorReducer
import cursola.rate.ExchangeRateDataSourceNetwork
import cursola.rate.ExchangeRateDataSourceSort
import cursola.rate.ExchangeRateDataSourceTodayGuard
import cursola.rate.FavoriteDataSource
import cursola.rate.FavoriteDataSourceAnalytics
import cursola.rate.FavoriteDataSourceDatabase
import cursola.rate.HistoryDataSource
import cursola.rate.HistoryDataSourceBaseline
import cursola.rate.HistoryDataSourceDatabase
import cursola.rate.HistoryDataSourceErrorReducer
import cursola.rate.HistoryDataSourceFailOnEmpty
import cursola.rate.HistoryDataSourceNetwork
import cursola.rate.LatestValueDataSource
import cursola.rate.LatestValueDataSourceAnalytics
import cursola.rate.LatestValueDataSourceDefault
import cursola.rate.LatestValueDataSourceImpl
import cursola.rate.analytics.AnalyticService
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
            ConversionRateDataSourceDatabaseToday(database),
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
        source = ExchangeRateDataSourceDatabase(database)
        source = ExchangeRateDataSourceTodayGuard(source)
        source = ExchangeRateDataSourceErrorReducer(
            source,
            ExchangeRateDataSourceNetwork(wrap(network, database)),
            ExchangeRateDataSourceDatabase(database)
        )
        source = ExchangeRateDataSourceSort(source)
        return source
    }

    @Provides
    fun favorites(
        database: ExchangeRateDatabase,
        analytics: AnalyticService
    ): FavoriteDataSource {
        var source: FavoriteDataSource
        source = FavoriteDataSourceDatabase(database)
        source = FavoriteDataSourceAnalytics(source, analytics)
        return source
    }

    @Provides
    fun latestValue(
        storage: Storage,
        analytics: AnalyticService
    ): LatestValueDataSource {
        var source: LatestValueDataSource
        source = LatestValueDataSourceImpl(storage)
        source = LatestValueDataSourceDefault(source)
        source = LatestValueDataSourceAnalytics(source, analytics)
        return source
    }

    @ScopeSinceInception
    @Provides
    fun historyValue(
        @ScopeSinceInception
        network: ExchangeRateService,
        database: ExchangeRateDatabase
    ): HistoryDataSource {
        var service = network
        service = ExchangeRateServiceSaving(service, database)
        service = ExchangeRateServiceCaching(service)
        service = ExchangeRateServicePeg(service)
        var source: HistoryDataSource
        source = HistoryDataSourceNetwork(service)
        source = HistoryDataSourceFailOnEmpty(source)
        source = HistoryDataSourceErrorReducer(source, HistoryDataSourceDatabase(database))
        source = HistoryDataSourceBaseline(source)
        return source
    }

    // ---

    private fun wrap(
        network: ExchangeRateService,
        database: ExchangeRateDatabase
    ): ExchangeRateService {
        var service = network
        service = ExchangeRateServiceBaseline(service)
        service = ExchangeRateServiceSaving(service, database)
        service = ExchangeRateServiceCaching(service)
        service = ExchangeRateServicePeg(service)
        return service
    }

    companion object {

        @TestOnly
        internal fun getInstance() = ExchangeRateModule()

    }

}