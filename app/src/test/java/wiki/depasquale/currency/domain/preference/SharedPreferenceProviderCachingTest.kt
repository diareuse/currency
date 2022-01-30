package wiki.depasquale.currency.domain.preference

import android.content.SharedPreferences
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.kotlin.whenever
import wiki.depasquale.currency.tooling.MockableTest
import kotlin.random.Random.Default.nextInt

class SharedPreferenceProviderCachingTest : MockableTest() {

    private lateinit var provider: SharedPreferenceProviderCaching

    @Mock
    lateinit var source: SharedPreferenceProvider

    @Mock
    lateinit var preferences: SharedPreferences

    override fun prepare() {
        super.prepare()
        provider = SharedPreferenceProviderCaching(source)
    }

    @Test
    fun `calls source only once`() {
        var callCounter = 0
        whenever(source.getSharedPreferences()).thenAnswer {
            callCounter++
            preferences
        }
        repeat(nextInt(2, 10)) {
            provider.getSharedPreferences()
        }
        assertThat(callCounter).isEqualTo(1)
    }

    @Test
    fun `returns the same instance`() {
        whenever(source.getSharedPreferences()).thenAnswer { Mockito.mock(SharedPreferences::class.java) }
        val instances = hashSetOf<SharedPreferences>()
        repeat(nextInt(2, 10)) {
            instances += provider.getSharedPreferences()
        }
        assertThat(instances).hasSize(1)
    }

}