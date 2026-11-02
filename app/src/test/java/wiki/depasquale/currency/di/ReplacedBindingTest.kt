package wiki.depasquale.currency.di

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import cursola.rate.ExchangeRate
import cursola.rate.ExchangeRateDataSource
import cursola.rate.view.ConvertedCurrency
import cursola.rate.view.ListingViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import spike.EntryPoint
import spike.Include
import spike.test.Replaces
import java.util.Currency
import java.util.Date
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class ReplacedBindingTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun test_graph_feeds_replaced_exchange_rates_to_listing_screen() = runTest {
        val viewModel = TestAppEntryPoint(context).viewModelEntryPointFactory.create(SavedStateHandle())
            .viewModels.getValue(ListingViewModel::class).get() as ListingViewModel

        assertEquals(listOf(ConvertedCurrency(FixedExchangeRateDataSource.rate, false)), viewModel.items.first())
    }

}

@Include
@Replaces(ExchangeRateDataSource::class)
class FixedExchangeRateDataSource : ExchangeRateDataSource {

    override suspend fun get() = listOf(rate)

    companion object {
        val rate = ExchangeRate(Currency.getInstance("XAU"), 0.42, Date(0))
    }

}

@EntryPoint
interface TestAppEntryPoint : AppEntryPoint {

    @EntryPoint.Factory
    interface Factory {
        fun create(context: Context): TestAppEntryPoint
    }

    companion object

}
