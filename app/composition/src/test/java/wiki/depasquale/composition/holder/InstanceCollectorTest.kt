package wiki.depasquale.composition.holder

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import wiki.depasquale.composition.extra.Alias
import wiki.depasquale.composition.tooling.nextString
import wiki.depasquale.composition.tooling.nextStringNot

class InstanceCollectorTest {

    @Test
    fun `returns assigned value without alias`() {
        val collector = createCollector()
        val key = nextString()
        val value = nextString()
        val alias: Alias? = null
        collector.set(key, alias, value)

        val storedValue = collector.get(key, alias)
        assertThat(storedValue).isNotNull()
        assertThat(storedValue).isEqualTo(value)
    }

    @Test
    fun `returns null without assigning value`() {
        val collector = createCollector()
        val value = collector.get(nextString())

        assertThat(value).isNull()
    }

    @Test
    fun `returns assigned value with alias`() {
        val collector = createCollector()
        val key = nextString()
        val value = nextString()
        val alias = nextString()
        collector.set(key, Alias(alias), value)

        val storedValue = collector.get(key, Alias(alias))
        assertThat(storedValue).isNotNull()
        assertThat(storedValue).isEqualTo(value)
    }

    @Test
    fun `returns null on alias assigned value without alias`() {
        val collector = createCollector()
        val key = nextString()
        val value = nextString()
        val alias = nextString()
        collector.set(key, Alias(alias), value)

        val storedValue = collector.get(key, null)
        assertThat(storedValue).isNull()
    }

    @Test
    fun `returns null on assigned value with alias`() {
        val collector = createCollector()
        val key = nextString()
        val value = nextString()
        val alias = nextString()
        collector.set(key, null, value)

        val storedValue = collector.get(key, Alias(alias))
        assertThat(storedValue).isNull()
    }

    @Test
    fun `returns null on alias assigned value with different alias`() {
        val collector = createCollector()
        val key = nextString()
        val value = nextString()
        val alias = nextString()
        val alias2 = nextStringNot(alias)
        collector.set(key, Alias(alias), value)

        val storedValue = collector.get(key, Alias(alias2))
        assertThat(storedValue).isNull()
    }

    @Test
    fun `returns values if two instances assigned different aliases`() {
        val collector = createCollector()
        val key = nextString()
        val value = nextString()
        val value2 = nextStringNot(value)
        val alias = nextString()
        val alias2 = nextStringNot(alias)

        collector.set(key, Alias(alias), value)
        collector.set(key, Alias(alias2), value2)

        assertThat(collector.get(key, Alias(alias))).isEqualTo(value)
        assertThat(collector.get(key, Alias(alias2))).isEqualTo(value2)
    }

    @Test
    fun `returns overwritten value`() {
        val collector = createCollector()
        val key = nextString()
        val value = nextString()

        collector.set(key, null, value)

        val value2 = nextStringNot(value)

        collector.set(key, null, value2)

        assertThat(collector.get(key)).isEqualTo(value2)
    }

    // ---

    private fun createCollector(): InstanceCollector<String, String> {
        return InstanceCollector()
    }

}