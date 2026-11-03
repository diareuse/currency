package wiki.depasquale.currency.platform.tile

import android.app.StatusBarManager
import android.content.ComponentName
import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements
import org.robolectric.shadows.ShadowStatusBarManager

@Implements(StatusBarManager::class)
class RecordingStatusBarManager : ShadowStatusBarManager() {

    val listeningRequests = mutableListOf<ComponentName>()

    @Implementation
    fun requestTileServiceListeningState(component: ComponentName) {
        listeningRequests += component
    }

}
