package wiki.depasquale.currency.di

import wiki.depasquale.currency.platform.link.ConverterLaunchIntent
import wiki.depasquale.currency.platform.tile.QuickRateSource
import wiki.depasquale.currency.platform.tile.TileRenderer
import wiki.depasquale.currency.platform.tile.TopFavoritePair

interface TileEntryPoint {

    val quickRate: QuickRateSource
    val tileRenderer: TileRenderer
    val converterLaunchIntent: ConverterLaunchIntent
    val topFavoritePair: TopFavoritePair

}
