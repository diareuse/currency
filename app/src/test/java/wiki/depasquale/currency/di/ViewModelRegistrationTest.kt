package wiki.depasquale.currency.di

import android.content.Context
import org.junit.Test
import org.mockito.kotlin.mock
import java.io.File
import java.util.zip.ZipFile
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ViewModelRegistrationTest {

    @Test
    fun every_spike_view_model_on_the_classpath_is_registered_in_the_app_graph() {
        val generated = spikeViewModelClassNames()
        val registered = AppEntryPoint(mock<Context>()).viewModels.keys.map { it.java.name }.toSet()

        assertTrue(generated.isNotEmpty())
        assertEquals(generated, registered)
    }

    private fun spikeViewModelClassNames(): Set<String> = System.getProperty("java.class.path").orEmpty()
        .split(File.pathSeparator)
        .map(::File)
        .flatMap(::classEntries)
        .filter { it.endsWith(GENERATED_SUFFIX) }
        .map { it.removeSuffix(GENERATED_SUFFIX).replace('/', '.') }
        .toSet()

    private fun classEntries(root: File): List<String> = when {
        root.isDirectory -> root.walk().filter { it.isFile }.map { it.relativeTo(root).invariantSeparatorsPath }.toList()
        root.isFile && root.extension == "jar" -> ZipFile(root).use { zip -> zip.entries().toList().map { it.name } }
        else -> emptyList()
    }

    private companion object {
        const val GENERATED_SUFFIX = "_SpikeViewModelKt.class"
    }

}
