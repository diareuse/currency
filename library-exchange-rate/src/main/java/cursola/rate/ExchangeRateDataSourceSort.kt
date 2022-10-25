package cursola.rate

internal class ExchangeRateDataSourceSort(
    private val origin: ExchangeRateDataSource
) : ExchangeRateDataSource {

    override suspend fun get(): List<ExchangeRate> {
        return origin.get().sortedBy { it.currency.displayName }
    }

}