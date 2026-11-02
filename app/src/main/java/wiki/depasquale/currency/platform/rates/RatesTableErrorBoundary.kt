package wiki.depasquale.currency.platform.rates

import android.net.Uri
import kotlin.coroutines.cancellation.CancellationException

internal class RatesTableErrorBoundary(
    private val origin: RatesTable
) : RatesTable by origin {

    override suspend fun rows(uri: Uri): RatesRows = try {
        origin.rows(uri)
    } catch (e: CancellationException) {
        throw e
    } catch (e: IllegalArgumentException) {
        throw e
    } catch (e: UnsupportedOperationException) {
        throw e
    } catch (e: IllegalStateException) {
        throw e
    } catch (_: Throwable) {
        RatesRows(origin.columns(uri), emptyList())
    }

}
