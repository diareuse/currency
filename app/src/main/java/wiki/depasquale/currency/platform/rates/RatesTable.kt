package wiki.depasquale.currency.platform.rates

import android.net.Uri

interface RatesTable {

    fun type(uri: Uri): String?

    @Throws(IllegalArgumentException::class)
    fun columns(uri: Uri): List<String>

    @Throws(IllegalArgumentException::class)
    suspend fun rows(uri: Uri): RatesRows

}
