package wiki.depasquale.composition.instance

import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import wiki.depasquale.composition.extra.Parameters
import wiki.depasquale.composition.tooling.getNoopScope
import kotlin.random.Random.Default.nextInt

class CreatorSingleTest {

    @Test
    fun `returns same value on single thread`() {
        val creator = CreatorSingle { Any() }
        val scope = getNoopScope()
        val firstValue = creator.getValue(scope)

        assertThat(firstValue).isSameInstanceAs(creator.getValue(scope))
    }

    @Test
    fun `returns same value on 2 concurrent threads`() {
        val creator = CreatorSingle { Any() }
        val scope = getNoopScope()
        val instances = mutableListOf<Any>()

        val thread1 = Thread { repeat(20) { instances += creator.getValue(scope) } }
        val thread2 = Thread { repeat(20) { instances += creator.getValue(scope) } }

        thread1.start()
        thread2.start()

        thread1.join()
        thread2.join()

        assertThat(instances).isNotEmpty()
        for ((index, instance) in instances.withIndex()) {
            val next = instances.getOrNull(index + 1)
            if (next != null) {
                assertThat(instance).isSameInstanceAs(next)
            }
        }
    }

    @Test
    fun `returns different value when used with different parameters`() {
        val creator = CreatorSingle { (value: Int) -> ParametersTest.Value(value) }
        val scope = getNoopScope()
        var value = creator.getValue(scope, Parameters(1))

        Truth.assertThat(value).isEqualTo(ParametersTest.Value(1))

        value = creator.getValue(scope, Parameters(2))

        Truth.assertThat(value).isEqualTo(ParametersTest.Value(2))
    }

    @Test
    fun `returns same value when used with empty params`() {
        val creator = CreatorSingle { ParametersTest.Value(nextInt()) }
        val scope = getNoopScope()
        assertThat(creator.getValue(scope, Parameters())).isSameInstanceAs(
            creator.getValue(
                scope,
                Parameters()
            )
        )
    }

    @Test
    fun `returns same value when used with some params`() {
        val creator = CreatorSingle { (_: Int) -> ParametersTest.Value(nextInt()) }
        val scope = getNoopScope()
        assertThat(
            creator.getValue(
                scope,
                Parameters(100)
            )
        ).isSameInstanceAs(creator.getValue(scope, Parameters(100)))
    }

}