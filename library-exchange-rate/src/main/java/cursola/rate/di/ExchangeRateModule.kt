package cursola.rate.di

import cursola.rate.ConversionRateDataSource
import cursola.rate.ConversionRateDataSourceDatabase
import cursola.rate.ConversionRateDataSourceErrorReducer
import cursola.rate.ConversionRateDataSourceNetwork
import cursola.rate.ExchangeRateDataSource
import cursola.rate.ExchangeRateDataSourceDatabase
import cursola.rate.ExchangeRateDataSourceErrorReducer
import cursola.rate.ExchangeRateDataSourceNetwork
import cursola.rate.FavoriteDataSource
import cursola.rate.FavoriteDataSourceDatabase
import cursola.rate.database.ExchangeRateDatabase
import cursola.rate.network.ExchangeRateService
import cursola.rate.network.ExchangeRateServiceBaseline
import cursola.rate.network.ExchangeRateServiceCaching
import cursola.rate.network.ExchangeRateServicePeg
import cursola.rate.network.ExchangeRateServiceSaving
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
        return ExchangeRateDataSourceErrorReducer(
            ExchangeRateDataSourceNetwork(wrap(network, database)),
            ExchangeRateDataSourceDatabase(database)
        )
    }

    @Provides
    fun favorites(
        database: ExchangeRateDatabase
    ): FavoriteDataSource {
        return FavoriteDataSourceDatabase(database)
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