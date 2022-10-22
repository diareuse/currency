package cursola.rate

import java.util.Currency

internal class ConversionRateDataSourceErrorReducer(
    private vararg val sources: ConversionRateDataSource
) : ConversionRateDataSource {

    override suspend fun get(from: Currency, to: Currency): Double {
        val exception = ExchangeRateError.NoSourceAvailable()
        return sources.fold(Result.failure<Double>(exception)) { acc, source ->
            acc.recoverCatching { source.get(from, to) }
        }.getOrThrow()
    }

}