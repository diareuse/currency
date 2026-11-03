package wiki.depasquale.currency.platform.tile

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.service.quicksettings.TileService
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import wiki.depasquale.currency.di.appGraph
import wiki.depasquale.currency.platform.link.CurrencyPair

class QuickRateTileService : TileService() {

    private val scope = MainScope()
    private var listening: Job? = null
    private var shown: QuickRate? = null

    override fun onTileAdded() {
        super.onTileAdded()
        runCatching { requestListeningState(this, ComponentName(this, javaClass)) }
    }

    override fun onStartListening() {
        super.onStartListening()
        listening?.cancel()
        shown = null
        listening = scope.launch {
            val rate = appGraph.quickRate.current()
            val tile = qsTile ?: return@launch
            appGraph.tileRenderer.render(tile, rate)
            shown = rate
        }
    }

    override fun onClick() {
        super.onClick()
        val rendered = shown
        if (rendered != null) {
            openConverter(rendered.pair)
        } else {
            scope.launch { openConverter(appGraph.topFavoritePair.current()) }
        }
    }

    private fun openConverter(pair: CurrencyPair?) {
        runCatching {
            val intent = appGraph.converterLaunchIntent.create(pair)
            if (isLocked) unlockAndRun { open(intent) } else open(intent)
        }
    }

    private fun open(intent: Intent) {
        runCatching { collapseInto(intent) }
    }

    private fun collapseInto(intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            val flags = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            startActivityAndCollapse(PendingIntent.getActivity(this, 0, intent, flags))
        } else {
            @Suppress("DEPRECATION")
            startActivityAndCollapse(intent)
        }
    }

    override fun onStopListening() {
        listening?.cancel()
        super.onStopListening()
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

}
