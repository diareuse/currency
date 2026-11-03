package wiki.depasquale.currency.platform.tile

import android.app.StatusBarManager
import android.content.ComponentName
import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements
import org.robolectric.shadows.ShadowStatusBarManager

@Implements(StatusBarManager::class)
class RejectingStatusBarManager : ShadowStatusBarManager() {

    @Implementation
    fun requestTileServiceListeningState(component: ComponentName) {
        throw IllegalArgumentException("Tile $component was never added")
    }

}
