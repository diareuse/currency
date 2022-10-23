package cursola.rate.view.util

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlin.time.Duration

internal inline fun <T> repeatingFlow(every: Duration, crossinline body: suspend () -> T) = flow {
    emitEvery(every) { body() }
}

internal suspend inline fun <T> FlowCollector<T>.emitEvery(duration: Duration, body: () -> T) {
    repeatEvery(duration) {
        emit(body())
    }
}

internal suspend inline fun repeatEvery(duration: Duration, body: () -> Unit) {
    while (currentCoroutineContext().isActive) {
        body()
        delay(duration)
    }
}