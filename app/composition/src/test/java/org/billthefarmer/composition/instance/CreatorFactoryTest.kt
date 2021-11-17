package org.billthefarmer.composition.instance

import com.google.common.truth.Truth
import org.billthefarmer.composition.tooling.getNoopScope
import org.junit.Test

class CreatorFactoryTest {

    @Test
    fun `returns different value on single thread`() {
        val creator = CreatorFactory { Any() }
        val scope = getNoopScope()
        val firstValue = creator.getValue(scope)

        Truth.assertThat(firstValue).isNotSameInstanceAs(creator.getValue(scope))
    }

    @Test
    fun `returns different value on 2 concurrent threads`() {
        val creator = CreatorFactory { Any() }
        val scope = getNoopScope()
        val instances = mutableListOf<Any>()

        val thread1 = Thread { repeat(20) { instances += creator.getValue(scope) } }
        val thread2 = Thread { repeat(20) { instances += creator.getValue(scope) } }

        thread1.start()
        thread2.start()

        thread1.join()
        thread2.join()

        Truth.assertThat(instances).isNotEmpty()
        for (instance in instances) {
            val sameInstances = instances.count { it === instance }
            Truth.assertThat(sameInstances).isEqualTo(1)
        }
    }

}