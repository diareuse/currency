package wiki.depasquale.currency.di

import androidx.lifecycle.SavedStateHandle
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import cursola.rate.view.ConvertedCurrency
import cursola.rate.view.ListingViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import wiki.depasquale.currency.App
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class ReplacedBindingTest {

    private val app = ApplicationProvider.getApplicationContext<App>()

    @Test
    fun test_graph_feeds_replaced_exchange_rates_to_listing_screen() = runTest {
        val viewModel = app.appGraph.viewModelEntryPointFactory.create(SavedStateHandle())
            .viewModels.getValue(ListingViewModel::class).get() as ListingViewModel

        assertEquals(listOf(ConvertedCurrency(FixedExchangeRateDataSource.rate, false)), viewModel.items.first())
    }

}
