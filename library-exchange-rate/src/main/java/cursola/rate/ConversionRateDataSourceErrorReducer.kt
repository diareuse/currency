package cursola.rate

import java.util.Currency

internal class ConversionRateDataSourceErrorReducer(
    private vararg val sources: ConversionRateDataSource
) : ConversionRateDataSource {

    override suspend fun get(from: Currency, to: Currency): Double {
        var latestError: Throwable? = null
        for (source in sources)
            try {
                return source.get(from, to)
            } catch (e: Throwable) {
                e.printStackTrace()
                latestError = e
                continue
            }
        throw latestError ?: NoSuchElementException("No sources are available")
    }

}