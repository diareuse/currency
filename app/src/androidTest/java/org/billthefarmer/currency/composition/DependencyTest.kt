package org.billthefarmer.currency.composition

import org.junit.Test

class DependencyTest {

    @Test
    fun testAllDependenciesResolve() {
        val compositor = Dependency.compositor
        val entries = compositor.getKeys()

        for ((type, aliases) in entries) {
            for (alias in aliases) {
                compositor.get(type, alias)
            }
        }
    }

}