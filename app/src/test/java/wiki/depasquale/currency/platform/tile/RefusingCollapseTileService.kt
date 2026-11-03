package wiki.depasquale.currency.platform.tile

import android.app.PendingIntent
import android.service.quicksettings.TileService
import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements
import org.robolectric.shadows.ShadowTileService

@Implements(TileService::class)
class RefusingCollapseTileService : ShadowTileService() {

    @Implementation
    fun startActivityAndCollapse(pendingIntent: PendingIntent) {
        throw IllegalStateException("Tile service is not bound to System UI")
    }

}
