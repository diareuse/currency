package wiki.depasquale.currency.platform.rates

import android.net.Uri
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.cancellation.CancellationException

internal class RatesTableErrorBoundary(
    private val origin: RatesTable
) : RatesTable by origin {

    override suspend fun rows(uri: Uri): RatesRows = try {
        origin.rows(uri)
    } catch (_: CancellationException) {
        currentCoroutineContext().ensureActive()
        empty(uri)
    } catch (e: IllegalArgumentException) {
        throw e
    } catch (e: UnsupportedOperationException) {
        throw e
    } catch (_: Exception) {
        empty(uri)
    }

    private fun empty(uri: Uri) = RatesRows(origin.columns(uri), emptyList())

}
