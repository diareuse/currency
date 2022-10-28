package cursola.rate

import cursola.rate.analytics.AnalyticService

internal class LatestValueDataSourceAnalytics(
    private val origin: LatestValueDataSource,
    private val analytics: AnalyticService
) : LatestValueDataSource {

    override var currency: String
        get() = origin.currency
        set(value) {
            origin.currency = value
            analytics.log("currency_selected", paramsOf(value))
        }
    override var value: String
        get() = origin.value
        set(value) {
            origin.value = value
            analytics.log("value_entered", paramsOf(value.toDoubleOrNull() ?: return))
        }

    private fun paramsOf(value: String) = mapOf(
        "value" to value
    )

    private fun paramsOf(value: Double) = mapOf(
        "value" to value
    )

}