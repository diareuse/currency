package wiki.depasquale.currency.platform.rates

import android.net.Uri
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration

internal class RatesTableTimeout(
    private val origin: RatesTable,
    private val timeout: Duration
) : RatesTable by origin {

    override suspend fun rows(uri: Uri): RatesRows {
        val columns = origin.columns(uri)
        return withTimeoutOrNull(timeout) { origin.rows(uri) } ?: RatesRows(columns, emptyList())
    }

}
