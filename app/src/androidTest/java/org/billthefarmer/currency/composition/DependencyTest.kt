package org.billthefarmer.currency.composition

import androidx.test.platform.app.InstrumentationRegistry
import org.billthefarmer.composition.extra.ParametersNotSpecifiedException
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
                try {
                    compositor.get(type, alias)
                } catch (e: ParametersNotSpecifiedException) {
                    println(e.message + " for type $type")
                }
            }
        }
    }

}