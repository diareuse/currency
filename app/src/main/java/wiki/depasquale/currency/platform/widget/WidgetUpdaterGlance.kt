package wiki.depasquale.currency.platform.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.updateAll

class WidgetUpdaterGlance(
    private val context: Context,
    private val widget: GlanceAppWidget
) : WidgetUpdater {

    override suspend fun update() = widget.updateAll(context)

}
