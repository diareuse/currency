package wiki.depasquale.currency.di

import androidx.lifecycle.SavedStateHandle
import spike.lifecycle.viewmodel.ViewModelEntryPoint

internal class SharedViewModelEntryPointFactory(
    private val entryPoint: ViewModelEntryPoint
) : ViewModelEntryPoint.Factory {

    override fun create(handle: SavedStateHandle): ViewModelEntryPoint = entryPoint

}
