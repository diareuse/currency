package wiki.depasquale.currency.di

import android.content.Context
import androidx.work.WorkerFactory
import spike.EntryPoint
import spike.lifecycle.viewmodel.ViewModelEntryPoint
import wiki.depasquale.currency.platform.SliceProvider

@EntryPoint
interface AppEntryPoint : ViewModelEntryPoint, SliceProvider.SliceEntryPoint {

    val workerFactory: WorkerFactory

    @EntryPoint.Factory
    interface Factory {
        fun create(context: Context): AppEntryPoint
    }

    companion object

}
