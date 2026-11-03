package wiki.depasquale.currency.platform

fun interface RatesUpdatedListener {

    suspend fun onRatesUpdated()

}
