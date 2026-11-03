package wiki.depasquale.currency.platform.rates

import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import cursola.rate.ExchangeRateError
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class RatesTableErrorBoundaryTest {

    private val uri = Uri.parse("content://rates/table")

    @Test
    fun rows_are_empty_when_a_not_found_error_escapes_the_table() = runTest {
        val boundary = RatesTableErrorBoundary(FailingTable(ExchangeRateError.NotFoundException("No rate")))

        assertEquals(RatesRows(COLUMNS, emptyList()), boundary.rows(uri))
    }

    @Test
    fun rows_are_empty_when_an_interruption_escapes_the_table() = runTest {
        val boundary = RatesTableErrorBoundary(FailingTable(InterruptedException("interrupted elsewhere")))

        assertEquals(RatesRows(COLUMNS, emptyList()), boundary.rows(uri))
    }

    private class FailingTable(private val failure: Exception) : RatesTable {

        override fun type(uri: Uri) = null

        override fun columns(uri: Uri) = COLUMNS

        override suspend fun rows(uri: Uri): RatesRows = throw failure

    }

    private companion object {
        val COLUMNS = listOf("code")
    }

}
