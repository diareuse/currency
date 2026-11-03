package wiki.depasquale.currency.platform

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class LatestValueChangedListenerLaunching(
    private val scope: CoroutineScope,
    private val action: suspend () -> Unit
) : LatestValueChangedListener {

    override fun onLatestValueChanged() {
        scope.launch { action() }
    }

}
