package cursola.rate.di

import cursola.rate.ConversionRateDataSource
import cursola.rate.ConversionRateDataSourceDatabase
import cursola.rate.ConversionRateDataSourceErrorReducer
import cursola.rate.ConversionRateDataSourceNetwork
import cursola.rate.ExchangeRateDataSource
import cursola.rate.ExchangeRateDataSourceDatabase
import cursola.rate.ExchangeRateDataSourceErrorReducer
import cursola.rate.ExchangeRateDataSourceNetwork
import cursola.rate.database.ExchangeRateDatabase
import cursola.rate.network.ExchangeRateService
import cursola.rate.network.ExchangeRateServiceSaving
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import org.jetbrains.annotations.TestOnly

@Module
@InstallIn(ActivityRetainedComponent::class)
internal abstract class ExchangeRateModule {

    @Provides
    fun conversionRate(
        network: ExchangeRateService,
        database: ExchangeRateDatabase
    ): ConversionRateDataSource {
        val network = ExchangeRateServiceSaving(network, database)
        return ConversionRateDataSourceErrorReducer(
            ConversionRateDataSourceNetwork(network),
            ConversionRateDataSourceDatabase(database)
        )
    }

    @Provides
    fun exchangeRate(
        network: ExchangeRateService,
        database: ExchangeRateDatabase
    ): ExchangeRateDataSource {
        val network = ExchangeRateServiceSaving(network, database)
        return ExchangeRateDataSourceErrorReducer(
            ExchangeRateDataSourceNetwork(network),
            ExchangeRateDataSourceDatabase(database)
        )
    }

    companion object {

        @TestOnly
        internal fun getInstance() = object : ExchangeRateModule() {}

    }

}