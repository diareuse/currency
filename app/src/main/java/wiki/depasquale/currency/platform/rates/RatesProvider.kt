package wiki.depasquale.currency.platform.rates

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import kotlinx.coroutines.runBlocking
import wiki.depasquale.currency.di.appGraph

class RatesProvider : ContentProvider() {

    private val table get() = checkNotNull(context).appGraph.ratesTable

    override fun onCreate() = true

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {
        require(selection.isNullOrEmpty()) { "Selection is not supported" }
        require(sortOrder.isNullOrEmpty()) { "Sort order is not supported" }
        val rows = rowsOf(uri).project(projection?.toList())
        return MatrixCursor(rows.columns.toTypedArray(), rows.rows.size).apply {
            rows.rows.forEach { addRow(it) }
        }
    }

    private fun rowsOf(uri: Uri) = try {
        runBlocking { table.rows(uri) }
    } catch (_: InterruptedException) {
        RatesRows(table.columns(uri), emptyList())
    } finally {
        Thread.interrupted()
    }

    override fun getType(uri: Uri) = table.type(uri)

    override fun insert(uri: Uri, values: ContentValues?): Uri? =
        throw UnsupportedOperationException(READ_ONLY)

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int =
        throw UnsupportedOperationException(READ_ONLY)

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int =
        throw UnsupportedOperationException(READ_ONLY)

    private companion object {
        const val READ_ONLY = "Rates are read only"
    }

}
