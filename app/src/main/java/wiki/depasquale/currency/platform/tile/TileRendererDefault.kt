package wiki.depasquale.currency.platform.tile

import android.content.Context
import android.os.Build
import android.service.quicksettings.Tile
import wiki.depasquale.currency.R
import wiki.depasquale.currency.platform.link.CurrencyPair
import java.util.Locale

class TileRendererDefault(
    private val context: Context,
    private val format: RateFormat
) : TileRenderer {

    override fun render(tile: Tile, rate: QuickRate) {
        when (rate) {
            QuickRate.NoPair -> tile.show(
                context.getString(R.string.tile_label),
                context.getString(R.string.tile_no_pair),
                Tile.STATE_INACTIVE
            )

            is QuickRate.Unavailable -> tile.show(
                label(rate.pair),
                context.getString(R.string.tile_unavailable),
                Tile.STATE_INACTIVE
            )

            is QuickRate.Shown -> tile.show(
                label(rate.pair),
                format.format(rate.rate, Locale.getDefault()),
                Tile.STATE_ACTIVE
            )
        }
        tile.updateTile()
    }

    private fun label(pair: CurrencyPair?) = when (pair) {
        null -> context.getString(R.string.tile_label)
        else -> context.getString(R.string.shortcut_pair_label, pair.from.currencyCode, pair.to.currencyCode)
    }

    private fun Tile.show(label: String, subtitle: String, state: Int) {
        this.label = label
        this.state = state
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            this.subtitle = subtitle
        }
    }

}
