package cursola.rate

internal class ExchangeRateDataSourceErrorReducer(
    private vararg val sources: ExchangeRateDataSource
) : ExchangeRateDataSource {

    override suspend fun get(): List<ExchangeRate> {
        val exception = ExchangeRateError.NoSourceAvailable()
        val initial = Result.failure<List<ExchangeRate>>(exception)
        return sources.fold(initial) { acc, source ->
            acc.recoverCatching { source.get() }
        }.getOrDefault(emptyList())
    }

}