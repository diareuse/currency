package org.billthefarmer.currency.composition

import com.google.common.truth.Truth.assertThat
import org.billthefarmer.currency.composition.instance.SingleCreator
import org.billthefarmer.currency.composition.tooling.getNoopScope
import org.junit.Test

class SingleCreatorTest {

    @Test
    fun `returns same value on single thread`() {
        val creator = SingleCreator { Any() }
        val scope = getNoopScope()
        val firstValue = creator.getValue(scope)

        assertThat(firstValue).isSameInstanceAs(creator.getValue(scope))
    }

    @Test
    fun `returns same value on 2 concurrent threads`() {
        val creator = SingleCreator { Any() }
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

}