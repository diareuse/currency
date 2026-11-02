package cursola.rate.di

import android.content.Context
import cursola.rate.ConversionRateDataSource
import cursola.rate.ExchangeRateDataSource
import cursola.rate.FavoriteDataSource
import cursola.rate.HistoryDataSource
import cursola.rate.LatestValueDataSource
import cursola.rate.database.ExchangeRateDatabase
import io.ktor.client.HttpClient
import spike.EntryPoint

@EntryPoint
internal interface ExchangeRateInfrastructure {
    val database: ExchangeRateDatabase
    val client: HttpClient

    @ExchangeRateLibrary
    val conversionRate: ConversionRateDataSource

    @ExchangeRateLibrary
    val exchangeRate: ExchangeRateDataSource

    @ExchangeRateLibrary
    val favorite: FavoriteDataSource

    @ExchangeRateLibrary
    val history: HistoryDataSource

    @ExchangeRateLibrary
    val latestValue: LatestValueDataSource

    @EntryPoint.Factory
    interface Factory {
        fun create(context: Context): ExchangeRateInfrastructure
    }

    companion object
}
