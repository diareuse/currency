package cursola.rate.di

import cursola.rate.ConversionRateDataSource
import cursola.rate.ConversionRateDataSourceDatabase
import cursola.rate.ConversionRateDataSourceErrorReducer
import cursola.rate.ConversionRateDataSourceNetwork
import cursola.rate.ExchangeRateDataSource
import cursola.rate.ExchangeRateDataSourceImpl
import cursola.rate.ExchangeRateServiceSaving
import cursola.rate.database.ExchangeRateDatabase
import cursola.rate.network.ExchangeRateService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

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
        return ExchangeRateDataSourceImpl(network, database)
    }

    companion object {

        internal fun getInstance() = object : ExchangeRateModule() {}

    }

}