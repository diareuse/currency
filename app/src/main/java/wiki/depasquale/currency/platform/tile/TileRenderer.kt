package wiki.depasquale.currency.platform.tile

import android.service.quicksettings.Tile

fun interface TileRenderer {

    fun render(tile: Tile, rate: QuickRate)

}
