package cursola.rate.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

suspend fun <T> T.runEffect(block: suspend CoroutineScope.(T) -> Unit): T = also { subject ->
    coroutineScope {
        launch {
            block(subject)
        }
    }
}