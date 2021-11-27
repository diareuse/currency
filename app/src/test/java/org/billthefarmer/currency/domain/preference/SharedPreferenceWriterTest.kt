package org.billthefarmer.currency.domain.preference

import android.content.SharedPreferences
import com.google.common.truth.Truth.assertThat
import org.billthefarmer.currency.domain.adapter.CurrencyPreferenceAdapter
import org.billthefarmer.currency.tooling.MockableTest
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.whenever
import java.util.*

class SharedPreferenceWriterTest : MockableTest() {

    @Mock
    lateinit var provider: SharedPreferenceProvider

    @Mock
    lateinit var preferences: SharedPreferences

    @Mock
    lateinit var preferencesEditor: SharedPreferences.Editor

    @Test
    fun `writes values to respective fields`() {
        val writer = SharedPreferenceWriter(provider, PreferenceModel::class)

        var writtenString = false
        var writtenStringSet = false
        var writtenInt = false
        var writtenLong = false
        var writtenFloat = false
        var writtenBoolean = false

        val model = PreferenceModel(
            string = "string key",
            stringSet = setOf("key", "value"),
            int = 1337,
            long = 1337L,
            float = 1.337F,
            boolean = true
        )

        whenever(provider.getSharedPreferences())
            .thenReturn(preferences)
        whenever(preferences.edit())
            .thenReturn(preferencesEditor)
        whenever(preferencesEditor.putString("string-key", model.string))
            .thenAnswer { writtenString = true;preferencesEditor }
        whenever(preferencesEditor.putStringSet("string-set-key", model.stringSet))
            .thenAnswer { writtenStringSet = true;preferencesEditor }
        whenever(preferencesEditor.putInt("int-key", model.int))
            .thenAnswer { writtenInt = true;preferencesEditor }
        whenever(preferencesEditor.putLong("long-key", model.long))
            .thenAnswer { writtenLong = true;preferencesEditor }
        whenever(preferencesEditor.putFloat("float-key", model.float))
            .thenAnswer { writtenFloat = true;preferencesEditor }
        whenever(preferencesEditor.putBoolean("boolean-key", model.boolean))
            .thenAnswer { writtenBoolean = true;preferencesEditor }
        whenever(preferencesEditor.apply()).then {}
        whenever(preferencesEditor.commit()).then {}

        writer.write(model)

        assertThat(writtenString).isTrue()
        assertThat(writtenStringSet).isTrue()
        assertThat(writtenInt).isTrue()
        assertThat(writtenLong).isTrue()
        assertThat(writtenFloat).isTrue()
        assertThat(writtenBoolean).isTrue()
    }

    @Test
    fun `writes single value with adapter`() {
        val reader = SharedPreferenceWriter(provider, PreferenceModelTyped::class)
        var writtenString = false

        val model = PreferenceModelTyped(
            Currency.getInstance("USD")
        )

        whenever(provider.getSharedPreferences())
            .thenReturn(preferences)
        whenever(preferences.edit())
            .thenReturn(preferencesEditor)
        whenever(preferencesEditor.putString("currency", model.currency.currencyCode))
            .thenAnswer { writtenString = true;preferencesEditor }

        reader.write(model)
        assertThat(writtenString).isTrue()
    }

    @Test
    fun `returns value set with adapter`() {
        val reader = SharedPreferenceWriter(provider, PreferenceModelTypedCollection::class)
        var writtenStringSet = false

        val model = PreferenceModelTypedCollection(
            listOf(Currency.getInstance("USD"))
        )

        whenever(provider.getSharedPreferences())
            .thenReturn(preferences)
        whenever(preferences.edit())
            .thenReturn(preferencesEditor)
        whenever(
            preferencesEditor.putStringSet(
                "currencies",
                model.currencies.map { it.currencyCode }.toSet()
            )
        )
            .thenAnswer { writtenStringSet = true;preferencesEditor }

        reader.write(model)
        assertThat(writtenStringSet).isTrue()
    }

    data class PreferenceModelTyped(
        @get:PreferenceKey("currency", CurrencyPreferenceAdapter::class)
        val currency: Currency
    )

    data class PreferenceModelTypedCollection(
        @get:PreferenceKey("currencies", CurrencyPreferenceAdapter::class)
        val currencies: Collection<Currency>
    )

    data class PreferenceModel(
        @get:PreferenceKey("string-key")
        val string: String,
        @get:PreferenceKey("string-set-key")
        val stringSet: Set<String>,
        @get:PreferenceKey("int-key")
        val int: Int,
        @get:PreferenceKey("long-key")
        val long: Long,
        @get:PreferenceKey("float-key")
        val float: Float,
        @get:PreferenceKey("boolean-key")
        val boolean: Boolean
    )

}