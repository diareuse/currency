package cursola.persistence.util

import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

suspend inline fun defer(crossinline body: suspend () -> Unit): Job {
    return coroutineScope {
        launch {
            body()
        }
    }
}

suspend inline fun <T> T.deferTask(crossinline task: suspend (T) -> Unit) = apply {
    defer { task(this) }
}