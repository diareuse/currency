package wiki.depasquale.currency.di

import cursola.rate.convert.MoneyFormat
import wiki.depasquale.currency.platform.widget.FavoritesSnapshotSource
import wiki.depasquale.currency.platform.widget.FavoritesWidgetRevision

interface WidgetEntryPoint {

    val favoritesSnapshot: FavoritesSnapshotSource
    val moneyFormat: MoneyFormat
    val widgetRevision: FavoritesWidgetRevision

}
