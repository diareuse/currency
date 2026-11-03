package wiki.depasquale.currency.platform.tile

import android.service.quicksettings.Tile

class TileRendererErrorBoundary(
    private val origin: TileRenderer
) : TileRenderer {

    @Suppress("TooGenericExceptionCaught")
    override fun render(tile: Tile, rate: QuickRate) {
        try {
            origin.render(tile, rate)
        } catch (_: Exception) {
            runCatching { origin.render(tile, QuickRate.Unavailable(rate.pair)) }
        }
    }

}
