package wiki.depasquale.currency.di

import android.content.Context
import cursola.rate.FavoriteDataSource
import cursola.rate.convert.CurrencyConverter
import cursola.rate.convert.CurrencyConverterTimeout
import kotlinx.coroutines.Dispatchers
import spike.Include
import wiki.depasquale.currency.platform.tile.QuickRateSource
import wiki.depasquale.currency.platform.tile.QuickRateSourceErrorBoundary
import wiki.depasquale.currency.platform.tile.QuickRateSourceOnContext
import wiki.depasquale.currency.platform.tile.QuickRateSourceTopPair
import wiki.depasquale.currency.platform.tile.RateFormatSignificant
import wiki.depasquale.currency.platform.tile.TileRenderer
import wiki.depasquale.currency.platform.tile.TileRendererDefault
import wiki.depasquale.currency.platform.tile.TileRendererErrorBoundary
import wiki.depasquale.currency.platform.tile.TopFavoritePair
import wiki.depasquale.currency.platform.tile.TopFavoritePairDefault
import wiki.depasquale.currency.platform.tile.TopFavoritePairErrorBoundary
import wiki.depasquale.currency.platform.tile.TopFavoritePairOnContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

private const val RATE_SIGNIFICANT_DIGITS = 4

internal data class QuickRateDeadline(val value: Duration)

@Include
internal fun quickRateDeadline() = QuickRateDeadline(2.seconds)

@Include
internal fun quickRateSource(
    favorite: FavoriteDataSource,
    converter: CurrencyConverter,
    deadline: QuickRateDeadline
): QuickRateSource {
    val bounded = CurrencyConverterTimeout(converter, deadline.value)
    val core = QuickRateSourceTopPair(TopFavoritePairDefault(favorite), bounded)
    val offMain = QuickRateSourceOnContext(core, Dispatchers.IO)
    return QuickRateSourceErrorBoundary(offMain)
}

@Include
internal fun topFavoritePair(favorite: FavoriteDataSource): TopFavoritePair =
    TopFavoritePairErrorBoundary(TopFavoritePairOnContext(TopFavoritePairDefault(favorite), Dispatchers.IO))

@Include
internal fun tileRenderer(context: Context): TileRenderer =
    TileRendererErrorBoundary(TileRendererDefault(context, RateFormatSignificant(RATE_SIGNIFICANT_DIGITS)))
