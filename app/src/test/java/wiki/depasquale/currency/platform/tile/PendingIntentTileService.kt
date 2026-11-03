package wiki.depasquale.currency.platform.tile

import android.app.PendingIntent
import android.content.Intent
import android.service.quicksettings.TileService
import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements
import org.robolectric.shadows.ShadowTileService

@Implements(TileService::class)
class PendingIntentTileService : ShadowTileService() {

    var collapsedInto: PendingIntent? = null
        private set

    @Implementation
    override fun startActivityAndCollapse(intent: Intent) {
        throw UnsupportedOperationException("Starting activity with Intent is not allowed from API 34")
    }

    @Implementation
    fun startActivityAndCollapse(pendingIntent: PendingIntent) {
        collapsedInto = pendingIntent
    }

}
