package wiki.depasquale.currency.platform.tile

import android.service.quicksettings.Tile
import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements

@Implements(Tile::class)
class FailingFirstUpdateTile {

    private var updates = 0

    @Implementation
    protected fun updateTile() {
        if (updates++ == 0) throw IllegalStateException("Tile is not bound")
    }

}
