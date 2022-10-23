package cursola.rate.view

import androidx.lifecycle.ViewModel
import cursola.rate.ConversionRateDataSource
import cursola.rate.ExchangeRateDataSource
import cursola.rate.FavoriteDataSource
import org.junit.Before
import org.mockito.kotlin.mock

internal abstract class AbstractViewModelTest<Model : ViewModel> {

    protected lateinit var conversion: ConversionRateDataSource
        private set
    protected lateinit var exchange: ExchangeRateDataSource
        private set
    protected lateinit var favorite: FavoriteDataSource
        private set
    protected lateinit var viewModel: Model
        private set

    @Before
    fun prepareInternal() {
        conversion = mock()
        exchange = mock()
        favorite = mock()
        viewModel = prepare()
    }

    protected abstract fun prepare(): Model

}