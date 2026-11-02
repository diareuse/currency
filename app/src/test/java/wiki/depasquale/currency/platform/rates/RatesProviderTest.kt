package wiki.depasquale.currency.platform.rates

import android.content.ContentValues
import android.content.pm.PermissionInfo
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import cursola.rate.ExchangeRate
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.Shadows.shadowOf
import wiki.depasquale.currency.App
import wiki.depasquale.currency.di.TestAppEntryPoint
import wiki.depasquale.currency.di.appGraph
import wiki.depasquale.cursola.contract.CurrencyContract
import wiki.depasquale.cursola.contract.CurrencyContract.Conversions
import wiki.depasquale.cursola.contract.CurrencyContract.Currencies
import wiki.depasquale.cursola.contract.CurrencyContract.LatestRates
import java.io.IOException
import java.util.Currency
import java.util.Date
import java.util.Locale
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class RatesProviderTest {

    private val app = ApplicationProvider.getApplicationContext<App>()
    private val graph get() = app.appGraph as TestAppEntryPoint
    private val resolver get() = app.contentResolver

    @Before
    fun setUp() {
        graph.exchangeRates.rates = listOf(
            ExchangeRate(EUR, 1.0, Date(SNAPSHOT)),
            ExchangeRate(USD, 1.25, Date(SNAPSHOT)),
            ExchangeRate(CZK, 25.0, Date(SNAPSHOT)),
        )
        Locale.setDefault(Locale.US)
        shadowOf(app).grantPermissions(CurrencyContract.PERMISSION_READ_RATES)
        Robolectric.setupContentProvider(RatesProvider::class.java, CurrencyContract.AUTHORITY)
    }

    @Test
    fun provider_is_exported_under_contract_authority_guarded_by_normal_read_permission_without_write_access() {
        val info = app.packageManager.resolveContentProvider(CurrencyContract.AUTHORITY, 0)!!
        val permission = app.packageManager.getPermissionInfo(CurrencyContract.PERMISSION_READ_RATES, 0)

        assertEquals(RatesProvider::class.java.name, info.name)
        assertTrue(info.exported)
        assertEquals(CurrencyContract.PERMISSION_READ_RATES, info.readPermission)
        assertNull(info.writePermission)
        assertEquals(false, info.grantUriPermissions)
        assertEquals(PermissionInfo.PROTECTION_NORMAL, permission.protection)
    }

    @Test
    fun currencies_lists_code_display_name_and_symbol_of_every_rated_currency() {
        val rows = query(Currencies.CONTENT_URI).use { it.rows() }

        assertEquals(Currencies.COLUMNS, columns(Currencies.CONTENT_URI))
        assertEquals(
            listOf(
                listOf("EUR", "Euro", "€"),
                listOf("USD", "US Dollar", "$"),
                listOf("CZK", "Czech Koruna", "CZK"),
            ),
            rows
        )
    }

    @Test
    fun latest_rates_default_to_euro_base_with_rate_and_snapshot_timestamp() {
        val rows = query(LatestRates.CONTENT_URI).use { it.rows() }

        assertEquals(LatestRates.COLUMNS, columns(LatestRates.CONTENT_URI))
        assertEquals(listOf(listOf("USD", 1.25, SNAPSHOT), listOf("CZK", 25.0, SNAPSHOT)), rows)
    }

    @Test
    fun latest_rates_rebase_on_requested_base_in_any_case() {
        val rows = query("${LatestRates.CONTENT_URI}?base=usd").use { it.rows() }

        assertEquals(listOf(listOf("EUR", 0.8, SNAPSHOT), listOf("CZK", 20.0, SNAPSHOT)), rows)
    }

    @Test
    fun latest_rates_are_empty_for_a_base_without_rate() {
        assertEquals(0, query("${LatestRates.CONTENT_URI}?base=GBP").use { it.count })
    }

    @Test
    fun convert_returns_amount_result_rate_and_timestamp_for_one_target() {
        val uri = "${Conversions.CONTENT_URI}?amount=10&from=EUR&to=USD"

        val rows = query(uri).use { it.rows() }

        assertEquals(Conversions.COLUMNS, columns(uri))
        assertEquals(listOf(listOf("EUR", "USD", 10.0, 12.5, 1.25, SNAPSHOT)), rows)
    }

    @Test
    fun convert_returns_one_row_per_repeated_target_in_request_order() {
        val rows = query("${Conversions.CONTENT_URI}?amount=2.5&from=usd&to=CZK&to=EUR").use { it.rows() }

        assertEquals(
            listOf(
                listOf("USD", "CZK", 2.5, 50.0, 20.0, SNAPSHOT),
                listOf("USD", "EUR", 2.5, 2.0, 0.8, SNAPSHOT),
            ),
            rows
        )
    }

    @Test
    fun convert_requires_a_target_so_favorites_never_leave_the_app() {
        runBlocking { graph.favorites.add(USD, 0) }

        assertFailsWith<IllegalArgumentException> { query("${Conversions.CONTENT_URI}?amount=1&from=EUR") }
        assertFailsWith<IllegalArgumentException> { query("${Conversions.CONTENT_URI}?amount=1&from=EUR&to=") }
    }

    @Test
    fun convert_accepts_up_to_64_targets() {
        val uri = "${Conversions.CONTENT_URI}?amount=1&from=EUR" + "&to=USD".repeat(64)

        assertEquals(1, query(uri).use { it.count })
    }

    @Test
    fun convert_rejects_more_than_64_targets_even_when_repeated() {
        val uri = "${Conversions.CONTENT_URI}?amount=1&from=EUR" + "&to=USD".repeat(65)

        assertFailsWith<IllegalArgumentException> { query(uri) }
    }

    @Test
    fun every_path_is_empty_when_rates_fail_with_io_error() {
        graph.exchangeRates.failure = IOException("offline")

        assertEveryPathIsEmpty()
    }

    @Test
    fun every_path_is_empty_when_rates_fail_with_database_error() {
        graph.exchangeRates.failure = SQLiteException("disk I/O error")

        assertEveryPathIsEmpty()
    }

    @Test
    fun convert_skips_targets_without_rate() {
        val rows = query("${Conversions.CONTENT_URI}?amount=1&from=EUR&to=GBP&to=USD").use { it.rows() }

        assertEquals(listOf("USD"), rows.map { it[1] })
    }

    @Test
    fun convert_is_empty_when_source_has_no_rate() {
        assertEquals(0, query("${Conversions.CONTENT_URI}?amount=1&from=GBP&to=USD").use { it.count })
    }

    @Test
    fun projection_limits_and_orders_returned_columns() {
        val uri = "${Conversions.CONTENT_URI}?amount=10&from=EUR&to=USD"

        val rows = resolver.query(Uri.parse(uri), arrayOf(Conversions.RESULT, Conversions.TO), null, null, null)!!
            .use { listOf(it.columnNames.toList()) + it.rows() }

        assertEquals(listOf(listOf(Conversions.RESULT, Conversions.TO), listOf(12.5, "USD")), rows)
    }

    @Test
    fun convert_rejects_missing_or_malformed_arguments() {
        listOf(
            "${Conversions.CONTENT_URI}?from=EUR&to=USD",
            "${Conversions.CONTENT_URI}?amount=ten&from=EUR&to=USD",
            "${Conversions.CONTENT_URI}?amount=1&to=USD",
            "${Conversions.CONTENT_URI}?amount=1&from=NOPE&to=USD",
            "${Conversions.CONTENT_URI}?amount=1&from=EUR&to=NOPE",
            "${Conversions.CONTENT_URI}?amount=1e999999999&from=EUR&to=USD",
            "${Conversions.CONTENT_URI}?amount=${"9".repeat(100)}&from=EUR&to=USD",
        ).forEach { uri ->
            assertFailsWith<IllegalArgumentException>(uri) { query(uri) }
        }
    }

    @Test
    fun latest_rates_reject_unknown_base() {
        assertFailsWith<IllegalArgumentException> { query("${LatestRates.CONTENT_URI}?base=NOPE") }
    }

    @Test
    fun unknown_path_is_rejected() {
        assertFailsWith<IllegalArgumentException> { query("${CurrencyContract.CONTENT_URI}/favorites") }
    }

    @Test
    fun unknown_path_has_no_type() {
        assertNull(resolver.getType(Uri.parse("${CurrencyContract.CONTENT_URI}/favorites")))
    }

    @Test
    fun unknown_projection_column_is_rejected() {
        assertFailsWith<IllegalArgumentException> {
            resolver.query(Uri.parse(Currencies.CONTENT_URI), arrayOf("nope"), null, null, null)
        }
    }

    @Test
    fun selection_is_rejected() {
        assertFailsWith<IllegalArgumentException> {
            resolver.query(Uri.parse(Currencies.CONTENT_URI), null, "code = ?", arrayOf("USD"), null)
        }
    }

    @Test
    fun sort_order_is_rejected() {
        assertFailsWith<IllegalArgumentException> {
            resolver.query(Uri.parse(Currencies.CONTENT_URI), null, null, null, "code ASC")
        }
    }

    @Test
    fun currencies_are_empty_without_known_rates() {
        graph.exchangeRates.rates = emptyList()

        assertEquals(0, query(Currencies.CONTENT_URI).use { it.count })
    }

    @Test
    fun convert_to_the_source_currency_keeps_the_amount() {
        val rows = query("${Conversions.CONTENT_URI}?amount=7&from=EUR&to=EUR").use { it.rows() }

        assertEquals(listOf(listOf("EUR", "EUR", 7.0, 7.0, 1.0, SNAPSHOT)), rows)
    }

    @Test
    fun convert_accepts_negative_amounts() {
        val rows = query("${Conversions.CONTENT_URI}?amount=-2&from=EUR&to=USD").use { it.rows() }

        assertEquals(listOf(listOf("EUR", "USD", -2.0, -2.5, 1.25, SNAPSHOT)), rows)
    }

    @Test
    fun convert_returns_one_row_per_distinct_target() {
        val rows = query("${Conversions.CONTENT_URI}?amount=1&from=EUR&to=USD&to=usd").use { it.rows() }

        assertEquals(listOf("USD"), rows.map { it[1] })
    }

    @Test
    fun every_path_is_empty_when_rates_do_not_arrive_in_time() {
        graph.exchangeRates.hanging = true

        assertEveryPathIsEmpty()
    }

    @Test
    fun every_path_reports_its_contract_type() {
        assertEquals(Currencies.CONTENT_TYPE, resolver.getType(Uri.parse(Currencies.CONTENT_URI)))
        assertEquals(LatestRates.CONTENT_TYPE, resolver.getType(Uri.parse(LatestRates.CONTENT_URI)))
        assertEquals(Conversions.CONTENT_TYPE, resolver.getType(Uri.parse(Conversions.CONTENT_URI)))
    }

    @Test
    fun writes_are_unsupported() {
        val uri = Uri.parse(Currencies.CONTENT_URI)

        assertFailsWith<UnsupportedOperationException> { resolver.insert(uri, ContentValues()) }
        assertFailsWith<UnsupportedOperationException> { resolver.update(uri, ContentValues(), null, null) }
        assertFailsWith<UnsupportedOperationException> { resolver.delete(uri, null, null) }
    }

    private fun assertEveryPathIsEmpty() {
        listOf(
            Currencies.CONTENT_URI,
            LatestRates.CONTENT_URI,
            "${Conversions.CONTENT_URI}?amount=1&from=EUR&to=USD&to=CZK",
        ).forEach { uri ->
            assertEquals(0, query(uri).use { it.count }, uri)
        }
    }

    private fun query(uri: String): Cursor = resolver.query(Uri.parse(uri), null, null, null, null)!!

    private fun columns(uri: String) = query(uri).use { it.columnNames.toList() }

    private fun Cursor.rows(): List<List<Any?>> = buildList {
        while (moveToNext()) add(List(columnCount) { value(it) })
    }

    private fun Cursor.value(index: Int): Any? = when (getType(index)) {
        Cursor.FIELD_TYPE_INTEGER -> getLong(index)
        Cursor.FIELD_TYPE_FLOAT -> getDouble(index)
        Cursor.FIELD_TYPE_NULL -> null
        else -> getString(index)
    }

    private companion object {
        const val SNAPSHOT = 1_700_000_000_000L
        val EUR: Currency = Currency.getInstance("EUR")
        val USD: Currency = Currency.getInstance("USD")
        val CZK: Currency = Currency.getInstance("CZK")
    }

}
