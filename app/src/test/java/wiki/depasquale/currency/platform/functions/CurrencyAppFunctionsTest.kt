package wiki.depasquale.currency.platform.functions

import android.database.sqlite.SQLiteException
import androidx.appfunctions.AppFunctionAppUnknownException
import androidx.appfunctions.AppFunctionData
import androidx.appfunctions.AppFunctionElementNotFoundException
import androidx.appfunctions.AppFunctionException
import androidx.appfunctions.AppFunctionInvalidArgumentException
import androidx.appfunctions.AppFunctionName
import androidx.appfunctions.AppFunctionSearchSpec
import androidx.appfunctions.ExecuteAppFunctionRequest
import androidx.appfunctions.ExecuteAppFunctionResponse
import androidx.appfunctions.metadata.AppFunctionObjectTypeMetadata
import androidx.appfunctions.testing.AppFunctionTestRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import cursola.rate.ExchangeRate
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.w3c.dom.Element
import wiki.depasquale.currency.App
import wiki.depasquale.currency.di.TestAppEntryPoint
import wiki.depasquale.currency.di.appGraph
import java.io.IOException
import java.util.Currency
import java.util.Date
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@RunWith(AndroidJUnit4::class)
@Config(sdk = [36], qualifiers = "en-rUS")
class CurrencyAppFunctionsTest {

    private val app = ApplicationProvider.getApplicationContext<App>()

    @get:Rule
    val functions = AppFunctionTestRule(app)

    private val manager get() = functions.getAppFunctionManager()
    private val graph get() = app.appGraph as TestAppEntryPoint

    @Before
    fun setUp() {
        graph.exchangeRates.rates = listOf(
            ExchangeRate(EUR, 1.0, PUBLISHED),
            ExchangeRate(USD, 1.08423, PUBLISHED),
            ExchangeRate(GBP, 0.86372, PUBLISHED),
            ExchangeRate(JPY, 162.847, PUBLISHED),
        )
    }

    @Test
    fun system_agents_find_the_four_currency_functions() = runBlocking<Unit> {
        val names = FUNCTIONS.map { AppFunctionName(app.packageName, id(it)) }.toSet()

        val found = manager.searchAppFunctions(AppFunctionSearchSpec(functionNames = names))

        assertEquals(names.map { it.functionIdentifier }.toSet(), found.map { it.id }.toSet())
    }

    @Test
    fun every_function_is_described_for_agents_in_the_schema_the_system_indexes() {
        val descriptions = indexedFunctionDescriptions()

        assertEquals(FUNCTIONS.map(::id).toSet(), descriptions.keys)
        for ((function, description) in descriptions) {
            assertTrue(description.isNotBlank(), "$function description")
        }
    }

    @Test
    fun every_parameter_result_and_result_field_is_described_for_agents() = runBlocking<Unit> {
        val names = FUNCTIONS.map { AppFunctionName(app.packageName, id(it)) }.toSet()

        val found = manager.searchAppFunctions(AppFunctionSearchSpec(functionNames = names))

        for (function in found) {
            assertTrue(function.response.description.isNotBlank(), "${function.id} result description")
            for (parameter in function.parameters) {
                assertTrue(parameter.description.isNotBlank(), "${function.id} parameter ${parameter.name} description")
            }
        }
        val results = found.flatMap { it.components.dataTypes.entries }.associate { it.key to it.value }
        assertEquals(RESULTS, results.keys)
        for ((name, type) in results) {
            assertIs<AppFunctionObjectTypeMetadata>(type)
            assertTrue(type.description.isNotBlank(), "$name description")
            for ((field, fieldType) in type.properties) {
                assertTrue(fieldType.description.isNotBlank(), "$name.$field description")
            }
        }
    }

    @Test
    fun convert_returns_the_unrounded_result_and_rate_with_a_display_text_rounded_to_the_target_currency() = runBlocking<Unit> {
        val response = execute("convert") { setDouble("amount", 12.5).setString("from", "EUR").setString("to", "GBP") }

        val conversion = returned(response)
        assertEquals(12.5, conversion.getDouble("amount"))
        assertEquals("EUR", conversion.getString("from"))
        assertEquals(10.7965, conversion.getDouble("result"), PRECISION)
        assertEquals("GBP", conversion.getString("to"))
        assertEquals(0.86372, conversion.getDouble("rate"), PRECISION)
        assertEquals("£10.80", conversion.getString("formatted"))
        assertEquals(PUBLISHED.time, conversion.getLong("timestampMillis"))
    }

    @Test
    fun convert_to_favorites_converts_into_each_favorite_most_important_first_leaving_out_the_source() = runBlocking<Unit> {
        favorites(USD, EUR, JPY)

        val response = execute("convertToFavorites") { setDouble("amount", 12.5).setString("from", "EUR") }

        val conversions = returnedList(response)
        assertEquals(listOf("USD", "JPY"), conversions.map { it.getString("to") })
        assertEquals(listOf("$13.55", "¥2,036"), conversions.map { it.getString("formatted") })
        assertEquals(listOf(13.552875, 2035.5875), conversions.map { it.getDouble("result") })
        assertEquals(listOf("EUR", "EUR"), conversions.map { it.getString("from") })
    }

    @Test
    fun list_currencies_lists_code_name_and_symbol_of_every_rated_currency_in_the_users_language() = runBlocking<Unit> {
        graph.exchangeRates.rates = listOf(ExchangeRate(EUR, 1.0, PUBLISHED), ExchangeRate(USD, 1.08423, PUBLISHED))

        val currencies = returnedList(execute("listCurrencies"))

        assertEquals(
            listOf(listOf("EUR", "Euro", "€"), listOf("USD", "US Dollar", "$")),
            currencies.map { listOf(it.getString("code"), it.getString("displayName"), it.getString("symbol")) }
        )
    }

    @Test
    fun latest_rates_lists_cross_rates_from_the_base_into_every_other_rated_currency() = runBlocking<Unit> {
        val rates = returnedList(execute("latestRates") { setString("base", "USD") })

        assertEquals(listOf("EUR", "GBP", "JPY"), rates.map { it.getString("code") })
        assertEquals(listOf("USD", "USD", "USD"), rates.map { it.getString("base") })
        assertEquals(1 / 1.08423, rates[0].getDouble("rate"), PRECISION)
        assertEquals(0.86372 / 1.08423, rates[1].getDouble("rate"), PRECISION)
        assertEquals(162.847 / 1.08423, rates[2].getDouble("rate"), PRECISION)
        assertEquals(listOf(PUBLISHED.time), rates.map { it.getLong("timestampMillis") }.distinct())
    }

    @Test
    fun convert_rejects_a_currency_code_that_is_not_iso_4217_as_an_invalid_argument() = runBlocking<Unit> {
        val response = execute("convert") { setDouble("amount", 1.0).setString("from", "EUR").setString("to", "ABC") }

        assertIs<AppFunctionInvalidArgumentException>(failure(response))
    }

    @Test
    fun convert_rejects_a_negative_amount_as_an_invalid_argument() = runBlocking<Unit> {
        val response = execute("convert") { setDouble("amount", -5.0).setString("from", "EUR").setString("to", "USD") }

        assertIs<AppFunctionInvalidArgumentException>(failure(response))
    }

    @Test
    fun convert_to_favorites_rejects_amounts_that_are_not_finite_numbers_as_invalid_arguments() = runBlocking<Unit> {
        favorites(USD)

        for (amount in listOf(Double.NaN, Double.POSITIVE_INFINITY)) {
            val response = execute("convertToFavorites") { setDouble("amount", amount).setString("from", "EUR") }

            assertIs<AppFunctionInvalidArgumentException>(failure(response), "amount $amount")
        }
    }

    @Test
    fun convert_reports_a_currency_without_an_exchange_rate_as_not_found() = runBlocking<Unit> {
        val response = execute("convert") { setDouble("amount", 1.0).setString("from", "EUR").setString("to", "CHF") }

        assertIs<AppFunctionElementNotFoundException>(failure(response))
    }

    @Test
    fun latest_rates_gives_up_on_a_hung_rate_source_and_reports_the_rates_as_not_found() = runBlocking<Unit> {
        graph.exchangeRates.hanging = true
        graph.appFunctionsDeadline.value = 100.milliseconds

        val response = withTimeout(TEST_LIMIT) { execute("latestRates") { setString("base", "EUR") } }

        assertIs<AppFunctionElementNotFoundException>(failure(response))
        assertTrue(graph.exchangeRates.abandoned)
    }

    @Test
    fun convert_gives_up_on_a_hung_rate_source_and_reports_the_rate_as_not_found() = runBlocking<Unit> {
        graph.exchangeRates.hanging = true
        graph.appFunctionsDeadline.value = 100.milliseconds

        val response = withTimeout(TEST_LIMIT) {
            execute("convert") { setDouble("amount", 1.0).setString("from", "EUR").setString("to", "USD") }
        }

        assertIs<AppFunctionElementNotFoundException>(failure(response))
        assertTrue(graph.exchangeRates.abandoned)
    }

    @Test
    fun latest_rates_reports_a_failing_rate_source_as_a_generic_app_error() = runBlocking<Unit> {
        graph.exchangeRates.failure = IOException("offline")

        val response = execute("latestRates") { setString("base", "EUR") }

        assertIs<AppFunctionAppUnknownException>(failure(response))
    }

    @Test
    fun convert_accepts_currency_codes_in_any_case_and_answers_with_upper_case_codes() = runBlocking<Unit> {
        val response = execute("convert") { setDouble("amount", 12.5).setString("from", "eur").setString("to", " gbp ") }

        val conversion = returned(response)
        assertEquals("EUR", conversion.getString("from"))
        assertEquals("GBP", conversion.getString("to"))
        assertEquals("£10.80", conversion.getString("formatted"))
    }

    @Test
    fun convert_to_favorites_reports_a_source_currency_without_an_exchange_rate_as_not_found() = runBlocking<Unit> {
        favorites(USD, GBP)

        val response = execute("convertToFavorites") { setDouble("amount", 1.0).setString("from", "CHF") }

        assertIs<AppFunctionElementNotFoundException>(failure(response))
    }

    @Test
    fun convert_to_favorites_returns_no_conversions_when_the_user_has_no_favorites() = runBlocking<Unit> {
        val response = execute("convertToFavorites") { setDouble("amount", 1.0).setString("from", "EUR") }

        assertEquals(emptyList(), returnedList(response))
    }

    @Test
    fun latest_rates_reports_a_base_currency_without_an_exchange_rate_as_not_found() = runBlocking<Unit> {
        val response = execute("latestRates") { setString("base", "CHF") }

        assertIs<AppFunctionElementNotFoundException>(failure(response))
    }

    @Test
    fun convert_to_favorites_reports_a_failing_favorites_read_as_a_generic_app_error() = runBlocking<Unit> {
        favorites(USD)
        graph.favoriteReadFailure.value = SQLiteException("database is locked")

        val response = execute("convertToFavorites") { setDouble("amount", 1.0).setString("from", "EUR") }

        assertIs<AppFunctionAppUnknownException>(failure(response))
    }

    private suspend fun execute(function: String, parameters: AppFunctionData.Builder.() -> AppFunctionData.Builder = { this }): ExecuteAppFunctionResponse {
        val metadata = manager.searchAppFunctions(
            AppFunctionSearchSpec(functionNames = setOf(AppFunctionName(app.packageName, id(function))))
        ).single()
        val data = AppFunctionData.Builder(metadata.parameters, metadata.components).parameters().build()
        return manager.executeAppFunction(ExecuteAppFunctionRequest(app.packageName, metadata.id, data))
    }

    private fun returned(response: ExecuteAppFunctionResponse): AppFunctionData {
        assertIs<ExecuteAppFunctionResponse.Success>(response, "Expected success but was ${(response as? ExecuteAppFunctionResponse.Error)?.error}")
        return checkNotNull(response.returnValue.getAppFunctionData(ExecuteAppFunctionResponse.Success.PROPERTY_RETURN_VALUE))
    }

    private fun returnedList(response: ExecuteAppFunctionResponse): List<AppFunctionData> {
        assertIs<ExecuteAppFunctionResponse.Success>(response, "Expected success but was ${(response as? ExecuteAppFunctionResponse.Error)?.error}")
        return checkNotNull(response.returnValue.getAppFunctionDataList(ExecuteAppFunctionResponse.Success.PROPERTY_RETURN_VALUE))
    }

    private fun failure(response: ExecuteAppFunctionResponse): AppFunctionException {
        assertIs<ExecuteAppFunctionResponse.Error>(response)
        return response.error
    }

    private fun favorites(vararg currencies: Currency) = runBlocking {
        currencies.forEachIndexed { index, currency -> graph.favorites.add(currency, currencies.size - index) }
    }

    private fun indexedFunctionDescriptions(): Map<String, String> {
        val schema = checkNotNull(javaClass.classLoader?.getResourceAsStream(SCHEMA_ASSET)) { "$SCHEMA_ASSET is not packaged" }
        val document = schema.use { DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(it) }
        val functions = document.getElementsByTagName("appfunction")
        return (0 until functions.length).map { functions.item(it) as Element }.associate { function ->
            function.childText("id") to function.childText("description")
        }
    }

    private fun Element.childText(tag: String): String {
        val children = childNodes
        return (0 until children.length).map(children::item)
            .filterIsInstance<Element>()
            .single { it.tagName == tag }
            .textContent
    }

    private fun id(function: String) = "$ENTRY_POINT#$function"

    private companion object {
        const val ENTRY_POINT = "wiki.depasquale.currency.platform.functions.CurrencyAppFunctions"
        val FUNCTIONS = listOf("convert", "convertToFavorites", "listCurrencies", "latestRates")
        const val PRECISION = 1e-9
        const val SCHEMA_ASSET = "assets/currency_functions.xml"
        val RESULTS = setOf("ConversionResult", "CurrencyInfoResult", "RateResult")
            .map { "wiki.depasquale.currency.platform.functions.$it" }.toSet()
        val TEST_LIMIT = 5.seconds
        val PUBLISHED = Date(1_700_000_000_000)
        val EUR: Currency = Currency.getInstance("EUR")
        val USD: Currency = Currency.getInstance("USD")
        val GBP: Currency = Currency.getInstance("GBP")
        val JPY: Currency = Currency.getInstance("JPY")
    }

}
