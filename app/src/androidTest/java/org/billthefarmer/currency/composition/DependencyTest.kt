package org.billthefarmer.currency.composition

import androidx.test.platform.app.InstrumentationRegistry
import org.junit.BeforeClass
import org.junit.Test

class DependencyTest {

    companion object {

        @JvmStatic
        @BeforeClass
        fun createDependencies() {
            val context = InstrumentationRegistry.getInstrumentation().context
            Dependency.start(context)
        }

    }

    @Test
    fun testAllDependenciesResolve() {
        val compositor = Dependency()
        val entries = compositor.getKeys()

        for ((type, aliases) in entries) {
            for (alias in aliases) {
                compositor.get(type, alias)
            }
        }
    }

}