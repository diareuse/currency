package wiki.depasquale.currency.domain.registry

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import wiki.depasquale.currency.tooling.MockableTest

class ObservableRegistryDefaultTest : MockableTest() {

    private lateinit var registry: ObservableRegistry<Any>

    override fun prepare() {
        super.prepare()
        registry = observableRegistryOf()
    }

    @Test
    fun `adding value projects value to state`() {
        val expected = Any()
        registry.add(expected)

        assertThat(registry.get().value).containsExactly(expected)
    }

    @Test
    fun `removing value removes value from state`() {
        val expected = Any()
        registry.add(expected)
        registry.remove(expected)

        assertThat(registry.get().value).isEmpty()
    }

}