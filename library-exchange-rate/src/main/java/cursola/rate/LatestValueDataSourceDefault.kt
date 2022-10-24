package cursola.rate

internal class LatestValueDataSourceDefault(
    private val origin: LatestValueDataSource
) : LatestValueDataSource {

    override var currency: String
        get() = origin.currency.ifBlank { "EUR" }
        set(value) {
            origin.currency = value
        }

    override var value: String
        get() = origin.value.ifBlank { "1" }
        set(value) {
            origin.value = value
        }

}