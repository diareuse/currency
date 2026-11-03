package wiki.depasquale.currency.platform.tile

fun interface QuickRateSource {

    suspend fun current(): QuickRate

}
