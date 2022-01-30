package wiki.depasquale.currency.domain.preference

import android.content.SharedPreferences
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.whenever
import wiki.depasquale.currency.domain.adapter.CurrencyPreferenceAdapter
import wiki.depasquale.currency.domain.adapter.CurrencyValueProvider
import wiki.depasquale.currency.tooling.MockableTest
import java.util.*

class SharedPreferenceReaderTest : MockableTest() {

    private val expectedString: String = "expected-string"
    private val expectedStringSet: Set<String> = setOf("expected1", "expected2", "expected3")
    private val expectedInt: Int = 1337
    private val expectedLong: Long = 1337L
    private val expectedFloat: Float = 1.337F
    private val expectedBoolean: Boolean = true
    private val expectedValues = mapOf(
        "test-string" to expectedString,
        "test-string-set" to expectedStringSet,
        "test-int" to expectedInt,
        "test-long" to expectedLong,
        "test-float" to expectedFloat,
        "test-boolean" to expectedBoolean,
    )

    @Mock
    lateinit var provider: SharedPreferenceProvider

    @Mock
    lateinit var preferences: SharedPreferences

    @Test
    fun `returns values for primitives`() {
        val reader = SharedPreferenceReader(provider, PreferenceModel::class)
        whenever(provider.getSharedPreferences()).thenReturn(preferences)
        whenever(preferences.all).thenReturn(expectedValues)

        val output = reader.read()
        assertThat(output.string).isEqualTo(expectedString)
        assertThat(output.stringSet).isEqualTo(expectedStringSet)
        assertThat(output.int).isEqualTo(expectedInt)
        assertThat(output.long).isEqualTo(expectedLong)
        assertThat(output.float).isEqualTo(expectedFloat)
        assertThat(output.boolean).isEqualTo(expectedBoolean)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `throws exception when no annotation`() {
        val reader = SharedPreferenceReader(provider, PreferenceModelNoAnnotation::class)
        whenever(provider.getSharedPreferences()).thenReturn(preferences)
        whenever(preferences.all).thenReturn(expectedValues)

        reader.read()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `throws exception when parameters is missing`() {
        val reader = SharedPreferenceReader(provider, PreferenceModel::class)
        whenever(provider.getSharedPreferences()).thenReturn(preferences)
        whenever(preferences.all).thenReturn(emptyMap())

        reader.read()
    }

    @Test
    fun `returns single value with adapter`() {
        val reader = SharedPreferenceReader(provider, PreferenceModelWithAdapter::class)
        val expectedValues = mapOf(
            "currency" to "USD"
        )
        whenever(provider.getSharedPreferences()).thenReturn(preferences)
        whenever(preferences.all).thenReturn(expectedValues)

        val output = reader.read()
        assertThat(output.currency).isEqualTo(Currency.getInstance("USD"))
    }

    @Test
    fun `returns value set with adapter`() {
        val reader = SharedPreferenceReader(provider, PreferenceModelSetWithAdapter::class)
        val expectedValues = mapOf(
            "currencies" to setOf("USD", "EUR")
        )
        whenever(provider.getSharedPreferences()).thenReturn(preferences)
        whenever(preferences.all).thenReturn(expectedValues)

        val output = reader.read()
        assertThat(output.currencies).containsExactly(
            Currency.getInstance("USD"),
            Currency.getInstance("EUR"),
        )
    }

    @Test
    fun `returns default value if null`() {
        val reader = SharedPreferenceReader(provider, PreferenceModelFallback::class)
        val expectedValues = emptyMap<String, Any?>()
        whenever(provider.getSharedPreferences()).thenReturn(preferences)
        whenever(preferences.all).thenReturn(expectedValues)
        val output = reader.read()
        assertThat(output.currencies).isEmpty()
    }

    data class PreferenceModelFallback(
        @PreferenceKey("test", defaultValueProvider = CurrencyValueProvider::class)
        val currencies: Collection<Currency>
    )

    data class PreferenceModelWithAdapter(
        @PreferenceKey("currency", CurrencyPreferenceAdapter::class)
        val currency: Currency
    )

    data class PreferenceModelSetWithAdapter(
        @PreferenceKey("currencies", CurrencyPreferenceAdapter::class)
        val currencies: Collection<Currency>
    )

    data class PreferenceModel(
        @PreferenceKey("test-string")
        val string: String,
        @PreferenceKey("test-string-set")
        val stringSet: Set<String>,
        @PreferenceKey("test-int")
        val int: Int,
        @PreferenceKey("test-long")
        val long: Long,
        @PreferenceKey("test-float")
        val float: Float,
        @PreferenceKey("test-boolean")
        val boolean: Boolean
    )

    data class PreferenceModelNoAnnotation(
        val string: String
    )

}