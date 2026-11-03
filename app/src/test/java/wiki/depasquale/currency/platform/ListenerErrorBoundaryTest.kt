package wiki.depasquale.currency.platform

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.io.IOException
import kotlin.test.assertFalse

class ListenerErrorBoundaryTest {

    @Test
    fun rates_updated_listener_failure_does_not_escape() = runBlocking {
        RatesUpdatedListenerErrorBoundary { throw IOException("favorites unavailable") }.onRatesUpdated()
    }

    @Test
    fun favorites_changed_listener_failure_does_not_escape() = runBlocking {
        FavoritesChangedListenerErrorBoundary { throw IOException("favorites unavailable") }.onFavoritesChanged()
    }

    @Test
    fun rates_updated_listener_cancelled_with_its_caller_does_not_let_the_caller_continue() = runBlocking {
        var continued = false
        val started = CompletableDeferred<Unit>()
        val listener = RatesUpdatedListenerErrorBoundary {
            started.complete(Unit)
            awaitCancellation()
        }

        val call = launch {
            listener.onRatesUpdated()
            continued = true
        }
        started.await()
        call.cancelAndJoin()

        assertFalse(continued)
    }

    @Test
    fun favorites_changed_listener_cancelled_with_its_caller_does_not_let_the_caller_continue() = runBlocking {
        var continued = false
        val started = CompletableDeferred<Unit>()
        val listener = FavoritesChangedListenerErrorBoundary {
            started.complete(Unit)
            awaitCancellation()
        }

        val call = launch {
            listener.onFavoritesChanged()
            continued = true
        }
        started.await()
        call.cancelAndJoin()

        assertFalse(continued)
    }

}
