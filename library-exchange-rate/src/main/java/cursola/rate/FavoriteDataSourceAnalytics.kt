package cursola.rate

import cursola.rate.analytics.AnalyticService
import java.util.Currency

internal class FavoriteDataSourceAnalytics(
    private val origin: FavoriteDataSource,
    private val analytics: AnalyticService,
) : FavoriteDataSource by origin {

    override suspend fun add(currency: Currency, priority: Int) {
        analytics.log("favorite_added", paramsOf(currency))
        origin.add(currency, priority)
    }

    override suspend fun remove(currency: Currency) {
        analytics.log("favorite_removed", paramsOf(currency))
        origin.remove(currency)
    }

    private fun paramsOf(currency: Currency) = mapOf(
        "value" to currency.currencyCode.uppercase()
    )

}