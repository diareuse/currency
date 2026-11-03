package wiki.depasquale.currency.platform.widget

class WidgetUpdaterRevision(
    private val revision: FavoritesWidgetRevision,
    private val origin: WidgetUpdater
) : WidgetUpdater {

    override suspend fun update() {
        revision.bump()
        origin.update()
    }

}
