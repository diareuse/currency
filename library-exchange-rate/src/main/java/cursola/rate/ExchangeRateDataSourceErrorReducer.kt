package cursola.rate

internal class ExchangeRateDataSourceErrorReducer(
    private vararg val sources: ExchangeRateDataSource
) : ExchangeRateDataSource {

    override suspend fun get(): List<ExchangeRate> {
        for (source in sources) {
            try {
                return source.get()
            } catch (e: Throwable) {
                e.printStackTrace()
                continue
            }
        }
        return emptyList()
    }

}