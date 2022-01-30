package wiki.depasquale.composition.scope

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import wiki.depasquale.composition.extra.Alias

class CompositionScopeTest {

    @Test
    fun `scope retrieves value`() {
        val instance = Any()
        val scope = object : CompositionScope {
            override fun <T> get(type: Class<T>, alias: Alias?, params: Array<out Any?>): T {
                assertThat(type).isSameInstanceAs(instance::class.java)
                assertThat(alias).isNull()
                return instance as T
            }
        }
        val value = scope.get<Any>()

        assertThat(value).isSameInstanceAs(instance)
    }

}