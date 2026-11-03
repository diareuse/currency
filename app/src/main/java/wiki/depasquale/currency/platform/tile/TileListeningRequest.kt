package wiki.depasquale.currency.platform.tile

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.service.quicksettings.TileService
import wiki.depasquale.currency.platform.FavoritesChangedListener
import wiki.depasquale.currency.platform.RatesUpdatedListener

class TileListeningRequest(
    private val context: Context,
    private val tile: Class<out Service>
) : RatesUpdatedListener, FavoritesChangedListener {

    override suspend fun onRatesUpdated() = request()

    override suspend fun onFavoritesChanged() = request()

    private fun request() {
        TileService.requestListeningState(context, ComponentName(context, tile))
    }

}
