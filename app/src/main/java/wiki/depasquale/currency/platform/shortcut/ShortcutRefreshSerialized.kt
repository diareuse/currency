package wiki.depasquale.currency.platform.shortcut

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ShortcutRefreshSerialized(
    private val origin: ShortcutRefresh,
    private val mutex: Mutex = Mutex()
) : ShortcutRefresh {

    override suspend fun refresh() = mutex.withLock { origin.refresh() }

}
