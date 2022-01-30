package wiki.depasquale.currency.domain.preference

import android.content.Context
import android.content.SharedPreferences
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.whenever
import wiki.depasquale.currency.tooling.MockableTest

class SharedPreferenceProviderImplTest : MockableTest() {

    private lateinit var provider: SharedPreferenceProviderImpl

    @Mock
    lateinit var context: Context

    @Mock
    lateinit var preferences: SharedPreferences

    override fun prepare() {
        super.prepare()
        provider = SharedPreferenceProviderImpl(context, "")
        whenever(context.packageName).thenReturn("")
        whenever(context.getSharedPreferences(".", Context.MODE_PRIVATE)).thenReturn(preferences)
    }

    @Test
    fun `returns preferences`() {
        val output = provider.getSharedPreferences()
        assertThat(output).isSameInstanceAs(preferences)
    }

}