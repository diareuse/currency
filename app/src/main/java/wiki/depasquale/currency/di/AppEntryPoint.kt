package wiki.depasquale.currency.di

import android.content.Context
import androidx.work.WorkerFactory
import cursola.rate.LatestValueDataSource
import cursola.rate.convert.AmountParser
import cursola.rate.convert.CurrencyConverter
import cursola.rate.convert.RatesQuery
import spike.EntryPoint
import spike.lifecycle.viewmodel.ViewModelEntryPoint
import wiki.depasquale.currency.platform.link.LaunchDestinationParser
import wiki.depasquale.currency.platform.rates.RatesTable

@EntryPoint
interface AppEntryPoint : ViewModelEntryPoint, TileEntryPoint {

    val workerFactory: WorkerFactory
    val converter: CurrencyConverter
    val amountParser: AmountParser
    val ratesQuery: RatesQuery
    val ratesTable: RatesTable
    val launchDestinations: LaunchDestinationParser

    fun latest(): LatestValueDataSource

    @EntryPoint.Factory
    interface Factory {
        fun create(context: Context): AppEntryPoint
    }

    companion object

}
