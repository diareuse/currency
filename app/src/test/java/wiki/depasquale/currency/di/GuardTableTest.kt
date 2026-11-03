package wiki.depasquale.currency.di

import org.junit.Test
import spike.factory.SingletonHolder
import java.io.File
import java.util.zip.ZipFile
import kotlin.test.assertTrue
import kotlin.test.fail

class GuardTableTest {

    @Test
    fun every_production_graph_program_guard_table_is_readable_by_the_spike_runtime() {
        assertGuardTablesReadable("AppEntryPoint")
    }

    @Test
    fun every_test_graph_program_guard_table_is_readable_by_the_spike_runtime() {
        assertGuardTablesReadable("TestAppEntryPoint")
    }

    private fun assertGuardTablesReadable(graph: String) {
        val tables = programClassNames(graph).mapNotNull { name -> guardsOf(name)?.let { name to it } }
        assertTrue(tables.isNotEmpty(), "No $graph program guard tables found on the test classpath")
        for ((name, guards) in tables) {
            runCatching { SingletonHolder().needed(guards) }
                .onFailure { fail("$name guard table is malformed: $it", it) }
        }
    }

    private fun guardsOf(className: String): String? {
        val field = Class.forName(className).declaredFields.firstOrNull { it.name == "guards" } ?: return null
        field.isAccessible = true
        return field.get(null) as String
    }

    private fun programClassNames(graph: String): List<String> {
        val pattern = Regex("^${PACKAGE_PATH}/${graph}_Program\\d+\\.class$")
        return classpathEntries()
            .flatMap(::classEntries)
            .filter(pattern::matches)
            .map { it.removeSuffix(".class").replace('/', '.') }
            .distinct()
    }

    private fun classpathEntries() = System.getProperty("java.class.path")
        .split(File.pathSeparator)
        .map(::File)
        .filter(File::exists)

    private fun classEntries(root: File): List<String> = when {
        root.isDirectory -> File(root, PACKAGE_PATH).listFiles().orEmpty().map { "$PACKAGE_PATH/${it.name}" }
        root.extension == "jar" -> ZipFile(root).use { zip -> zip.entries().toList().map { it.name } }
        else -> emptyList()
    }

    private companion object {
        const val PACKAGE_PATH = "wiki/depasquale/currency/di"
    }

}
