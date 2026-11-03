package wiki.depasquale.currency.platform

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class, kotlinx.coroutines.DelicateCoroutinesApi::class)
class FavoritesChangedListenerOnContextTest {

    @Test
    fun favorites_changed_listener_runs_on_the_given_context() = newSingleThreadContext("shortcuts").use { context ->
        var thread = ""
        val listener = FavoritesChangedListenerOnContext({ thread = Thread.currentThread().name }, context)

        runBlocking { listener.onFavoritesChanged() }

        assertEquals("shortcuts", thread.substringBefore(" @"))
    }

}
