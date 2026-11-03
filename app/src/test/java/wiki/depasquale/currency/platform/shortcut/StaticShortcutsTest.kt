package wiki.depasquale.currency.platform.shortcut

import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.XmlResourceParser
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.xmlpull.v1.XmlPullParser
import wiki.depasquale.currency.App
import wiki.depasquale.currency.screen.MainActivity
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@RunWith(AndroidJUnit4::class)
class StaticShortcutsTest {

    private val app = ApplicationProvider.getApplicationContext<App>()

    @Test
    fun launcher_activity_declares_convert_and_history_shortcuts_opening_main_activity() {
        val launcher = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER).setPackage(app.packageName)
        val activity = app.packageManager.queryIntentActivities(launcher, PackageManager.GET_META_DATA)
            .single().activityInfo
        val resource = activity.metaData?.getInt(SHORTCUTS_META_DATA) ?: 0

        assertEquals(MainActivity::class.java.name, activity.name)
        assertNotEquals(0, resource)
        assertEquals(
            listOf(
                Declared("convert", "Convert", Intent.ACTION_VIEW, app.packageName, MainActivity::class.java.name, "currency://convert"),
                Declared("history", "History", Intent.ACTION_VIEW, app.packageName, MainActivity::class.java.name, "currency://history"),
            ),
            app.resources.getXml(resource).use(::declared)
        )
    }

    private fun declared(parser: XmlResourceParser): List<Declared> {
        val shortcuts = mutableListOf<Declared>()
        var id = ""
        var label = ""
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.eventType != XmlPullParser.START_TAG) continue
            when (parser.name) {
                "shortcut" -> {
                    id = parser.attribute("shortcutId")
                    label = app.getString(parser.resource("shortcutShortLabel"))
                    assertNotEquals(0, parser.resource("icon"), "shortcut $id has no icon resource")
                }

                "intent" -> shortcuts += Declared(
                    id = id,
                    label = label,
                    action = parser.attribute("action"),
                    targetPackage = parser.attribute("targetPackage"),
                    targetClass = parser.attribute("targetClass"),
                    data = parser.attribute("data"),
                )
            }
        }
        return shortcuts
    }

    private fun XmlResourceParser.resource(name: String) = getAttributeResourceValue(ANDROID, name, 0)
        .also { assertNotEquals(0, it, "$name is not a resource reference") }

    private fun XmlResourceParser.attribute(name: String) = getAttributeValue(ANDROID, name).orEmpty()

    private data class Declared(
        val id: String,
        val label: String,
        val action: String,
        val targetPackage: String,
        val targetClass: String,
        val data: String,
    )

    private companion object {
        const val SHORTCUTS_META_DATA = "android.app.shortcuts"
        const val ANDROID = "http://schemas.android.com/apk/res/android"
    }

}
