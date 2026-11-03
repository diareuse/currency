package wiki.depasquale.currency.platform.widget

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import wiki.depasquale.currency.di.appGraph

class FavoritesWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val graph = context.appGraph
        val loadedAt = graph.widgetRevision.current.value
        val initial = graph.favoritesSnapshot.current()
        provideContent {
            val revision by graph.widgetRevision.current.collectAsState()
            val snapshot by produceState(initial, revision) {
                if (revision != loadedAt) value = graph.favoritesSnapshot.current()
            }
            FavoritesWidgetTheme {
                FavoritesWidgetContent(snapshot, graph.moneyFormat, graph.converterLaunchIntent)
            }
        }
    }

}
