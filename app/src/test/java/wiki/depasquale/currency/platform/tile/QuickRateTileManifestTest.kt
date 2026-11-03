package wiki.depasquale.currency.platform.tile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.service.quicksettings.TileService
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import wiki.depasquale.currency.App
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@RunWith(AndroidJUnit4::class)
class QuickRateTileManifestTest {

    private val app = ApplicationProvider.getApplicationContext<App>()

    @Test
    fun quick_settings_offers_an_exported_active_rate_tile_bound_only_by_the_system() {
        val intent = Intent(TileService.ACTION_QS_TILE).setPackage(app.packageName)

        val service = app.packageManager.queryIntentServices(intent, PackageManager.GET_META_DATA)
            .single().serviceInfo

        assertEquals(QuickRateTileService::class.java.name, service.name)
        assertEquals(true, service.exported)
        assertEquals(Manifest.permission.BIND_QUICK_SETTINGS_TILE, service.permission)
        assertEquals(true, service.metaData?.getBoolean(TileService.META_DATA_ACTIVE_TILE))
        assertNotEquals(0, service.icon)
        assertEquals("Exchange rate", service.loadLabel(app.packageManager).toString())
    }

}
