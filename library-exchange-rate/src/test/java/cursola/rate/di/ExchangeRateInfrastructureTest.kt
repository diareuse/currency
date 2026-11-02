package cursola.rate.di

import androidx.test.core.app.ApplicationProvider
import cursola.rate.ConversionRateDataSourceErrorReducer
import cursola.rate.ExchangeRateDataSourceSort
import cursola.rate.FavoriteDataSourceAnalytics
import cursola.rate.HistoryDataSourceBaseline
import cursola.rate.LatestValueDataSourceAnalytics
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertSame

@RunWith(AndroidJUnit4::class)
internal class ExchangeRateInfrastructureTest {

    private val graph = ExchangeRateInfrastructure(ApplicationProvider.getApplicationContext())

    @After
    fun release() {
        graph.database.close()
        graph.client.close()
    }

    @Test
    fun graph_reuses_one_database() {
        assertSame(graph.database, graph.database)
    }

    @Test
    fun graph_reuses_one_http_client() {
        assertSame(graph.client, graph.client)
    }

    @Test
    fun conversion_rate_falls_back_through_error_reducer() {
        assertIs<ConversionRateDataSourceErrorReducer>(graph.conversionRate)
    }

    @Test
    fun exchange_rates_come_sorted() {
        assertIs<ExchangeRateDataSourceSort>(graph.exchangeRate)
    }

    @Test
    fun favorite_changes_are_tracked_by_analytics() {
        assertIs<FavoriteDataSourceAnalytics>(graph.favorite)
    }

    @Test
    fun history_is_completed_with_baseline() {
        assertIs<HistoryDataSourceBaseline>(graph.history)
    }

    @Test
    fun latest_value_changes_are_tracked_by_analytics() {
        assertIs<LatestValueDataSourceAnalytics>(graph.latestValue)
    }

    @Test
    fun latest_currency_defaults_to_euro_when_nothing_was_stored() {
        assertEquals("EUR", graph.latestValue.currency)
    }

}
