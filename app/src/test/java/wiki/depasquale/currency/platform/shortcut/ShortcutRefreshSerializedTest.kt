package wiki.depasquale.currency.platform.shortcut

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import org.junit.Test
import kotlin.test.assertEquals

class ShortcutRefreshSerializedTest {

    @Test
    fun concurrent_refreshes_run_one_after_another() = runBlocking {
        val events = mutableListOf<String>()
        val release = CompletableDeferred<Unit>()
        var calls = 0
        val refresh = ShortcutRefreshSerialized(ShortcutRefresh {
            val call = ++calls
            events += "start $call"
            if (call == 1) release.await()
            events += "end $call"
        })

        val first = async { refresh.refresh() }
        val second = async { refresh.refresh() }
        repeat(3) { yield() }
        release.complete(Unit)
        listOf(first, second).awaitAll()

        assertEquals(listOf("start 1", "end 1", "start 2", "end 2"), events)
    }

}
