package wiki.depasquale.currency.platform.functions

interface CurrencyFunctions {

    suspend fun convert(amount: Double, from: String, to: String): ConversionResult

    suspend fun convertToFavorites(amount: Double, from: String): List<ConversionResult>

    suspend fun listCurrencies(): List<CurrencyInfoResult>

    suspend fun latestRates(base: String): List<RateResult>

}
