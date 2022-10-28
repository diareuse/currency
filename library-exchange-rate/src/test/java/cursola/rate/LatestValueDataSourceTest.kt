package cursola.rate

import cursola.rate.di.ExchangeRateModule
import org.junit.Test
import org.mockito.kotlin.verify
import kotlin.test.assertEquals

internal class LatestValueDataSourceTest : AbstractDataSourceTest() {

    private lateinit var source: LatestValueDataSource

    override fun prepare() {
        source = ExchangeRateModule.getInstance().latestValue(storage, analytics)
    }

    // ---

    @Test
    fun currency_returns_defaultValue() {
        assertEquals("EUR", source.currency)
    }

    @Test
    fun currency_sets() {
        source.currency = "USD"
        assertEquals("USD", source.currency)
    }

    @Test
    fun currency_sets_notifiesAnalytics() {
        source.currency = "USD"
        verify(analytics).log("currency_selected", mapOf("value" to "USD"))
    }

    @Test
    fun value_returns_defaultValue() {
        assertEquals("1", source.value)
    }

    @Test
    fun value_sets() {
        source.value = "3123.1"
        assertEquals("3123.1", source.value)
    }

    @Test
    fun value_sets_notifiesAnalytics() {
        source.value = "3123.1"
        verify(analytics).log("value_entered", mapOf("value" to 3123.1))
    }

}