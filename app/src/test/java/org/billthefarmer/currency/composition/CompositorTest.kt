package org.billthefarmer.currency.composition

import com.google.common.truth.Truth.assertThat
import org.billthefarmer.currency.composition.core.*
import org.billthefarmer.currency.composition.tooling.nextString
import org.billthefarmer.currency.composition.tooling.nextStringNot
import org.junit.Test

class CompositorTest {

    @Test
    fun `returns single value defined in builder`() {
        val instance = Any()
        val compositor = buildCompositor {
            single { instance }
        }

        val stored: Any = compositor.get()

        assertThat(stored).isSameInstanceAs(instance)
    }

    @Test
    fun `returns single value with alias defined in builder`() {
        val instance = Any()
        val alias = nextString()
        val compositor = buildCompositor {
            single(Alias(alias)) { instance }
        }

        val stored: Any = compositor.get(Alias(alias))

        assertThat(stored).isSameInstanceAs(instance)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `get single throws when used no alias on only aliased components`() {
        val instance = Any()
        val alias = nextString()
        val compositor = buildCompositor {
            single(Alias(alias)) { instance }
        }

        compositor.get<Any>()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `get single throws when used different alias on same component`() {
        val instance = Any()
        val alias = nextString()
        val alias2 = nextStringNot(alias)
        val compositor = buildCompositor {
            single(Alias(alias)) { instance }
        }

        compositor.get<Any>(Alias(alias2))
    }

    @Test
    fun `returns factory value defined in builder`() {
        val compositor = buildCompositor {
            factory { Any() }
        }

        assertThat(compositor.get<Any>()).isNotSameInstanceAs(compositor.get())
    }

    @Test
    fun `returns factory value with alias defined in builder`() {
        val compositor = buildCompositor {
            factory(Alias("")) { Any() }
        }

        assertThat(compositor.get<Any>(Alias(""))).isNotSameInstanceAs(compositor.get<Any>(Alias("")))
    }

    @Test
    fun `returns distinct single values defined in builder`() {
        val alias = nextString()
        val compositor = buildCompositor {
            single(Alias(alias)) { Any() }
            single { Any() }
        }

        assertThat(compositor.get<Any>()).isNotSameInstanceAs(compositor.get<Any>(Alias(alias)))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `get factory throws when used no alias on only aliased components`() {
        val alias = nextString()
        val compositor = buildCompositor {
            factory(Alias(alias)) { Any() }
        }

        compositor.get<Any>()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `get factory throws when used different alias on same component`() {
        val alias = nextString()
        val alias2 = nextStringNot(alias)
        val compositor = buildCompositor {
            factory(Alias(alias)) { Any() }
        }

        compositor.get<Any>(Alias(alias2))
    }

    @Test
    fun `returns resolved transitive dependency`() {
        val instance = Any()
        val compositor = buildCompositor {
            single { instance }
            factory { Transitive(get()) }
        }

        assertThat(compositor.get<Transitive>()).isInstanceOf(Transitive::class.java)
        assertThat(compositor.get<Transitive>().value).isSameInstanceAs(instance)
    }

    class Transitive(val value: Any)

}

