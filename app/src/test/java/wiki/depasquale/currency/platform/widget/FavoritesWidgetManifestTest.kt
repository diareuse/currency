package wiki.depasquale.currency.platform.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.xmlpull.v1.XmlPullParser
import wiki.depasquale.currency.App
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@RunWith(AndroidJUnit4::class)
class FavoritesWidgetManifestTest {

    private val app = ApplicationProvider.getApplicationContext<App>()

    @Test
    fun launcher_finds_an_exported_favorites_widget_receiver_with_provider_info() {
        val receiver = widgetReceiver()

        assertEquals(FavoritesWidgetReceiver::class.java.name, receiver.name)
        assertEquals(true, receiver.exported)
        assertNotEquals(0, receiver.metaData?.getInt(AppWidgetManager.META_DATA_APPWIDGET_PROVIDER) ?: 0)
    }

    @Test
    fun favorites_widget_provider_info_is_resizable_pushed_only_and_described() {
        val info = providerInfo()

        assertEquals("0", info[UPDATE_PERIOD])
        assertEquals("110.0dip", info[MIN_WIDTH])
        assertEquals("110.0dip", info[MIN_HEIGHT])
        assertEquals(
            AppWidgetProviderInfo.RESIZE_HORIZONTAL or AppWidgetProviderInfo.RESIZE_VERTICAL,
            Integer.decode(info.getValue(RESIZE_MODE))
        )
        assertEquals(
            "Your latest amount in your favorite currencies",
            app.getString(info.getValue(DESCRIPTION).removePrefix("@").toInt())
        )
    }

    private fun widgetReceiver(): ActivityInfo {
        val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE).setPackage(app.packageName)
        return app.packageManager.queryBroadcastReceivers(intent, PackageManager.GET_META_DATA).single().activityInfo
    }

    private fun providerInfo(): Map<String, String> {
        val receiver = widgetReceiver()
        val parser = app.resources.getXml(receiver.metaData.getInt(AppWidgetManager.META_DATA_APPWIDGET_PROVIDER))
        while (parser.next() != XmlPullParser.START_TAG) continue
        assertEquals("appwidget-provider", parser.name)
        return (0 until parser.attributeCount).associate { parser.getAttributeName(it) to parser.getAttributeValue(it) }
    }

    private companion object {
        const val UPDATE_PERIOD = "updatePeriodMillis"
        const val MIN_WIDTH = "minWidth"
        const val MIN_HEIGHT = "minHeight"
        const val RESIZE_MODE = "resizeMode"
        const val DESCRIPTION = "description"
    }

}
