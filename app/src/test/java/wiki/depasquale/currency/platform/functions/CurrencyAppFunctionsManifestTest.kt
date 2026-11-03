package wiki.depasquale.currency.platform.functions

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.w3c.dom.Element
import wiki.depasquale.currency.App
import java.io.File
import java.util.Properties
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class CurrencyAppFunctionsManifestTest {

    private val app = ApplicationProvider.getApplicationContext<App>()

    @Test
    @Config(sdk = [36])
    fun only_the_system_can_bind_the_exported_app_functions_service_on_android_16() {
        val service = appFunctionsService()

        assertEquals(SERVICE, service.name)
        assertEquals(BIND_PERMISSION, service.permission)
        assertTrue(service.exported)
        assertTrue(service.isEnabled)
    }

    @Test
    @Config(sdk = [36])
    fun app_functions_service_points_the_system_at_its_function_schemas() {
        val properties = serviceProperties()

        assertEquals("currency_functions-v1.xml", properties[V1_SCHEMA])
        assertEquals("currency_functions.xml", properties[V2_SCHEMA])
        assertEquals("app_functions_schema.xsd", properties[SCHEMA_DEFINITION])
    }

    @Test
    @Config(sdk = [35])
    fun app_functions_service_is_disabled_below_android_16() {
        val service = appFunctionsService()

        assertFalse(service.isEnabled)
    }

    private fun appFunctionsService(): ServiceInfo {
        val intent = Intent(SERVICE_INTERFACE).setPackage(app.packageName)
        val flags = PackageManager.GET_META_DATA or PackageManager.MATCH_DISABLED_COMPONENTS
        return app.packageManager.queryIntentServices(intent, flags).single().serviceInfo
    }

    private fun serviceProperties(): Map<String, String> {
        val config = Properties().apply {
            checkNotNull(ClassLoader.getSystemResourceAsStream(TEST_CONFIG)).use(::load)
        }
        val manifest = DocumentBuilderFactory.newInstance().apply { isNamespaceAware = true }
            .newDocumentBuilder()
            .parse(File(config.getProperty(MERGED_MANIFEST)))
        val services = manifest.getElementsByTagName("service")
        val service = (0 until services.length).map { services.item(it) as Element }
            .single { it.getAttributeNS(ANDROID, "name") == SERVICE }
        val properties = service.getElementsByTagName("property")
        return (0 until properties.length).map { properties.item(it) as Element }
            .associate { it.getAttributeNS(ANDROID, "name") to it.getAttributeNS(ANDROID, "value") }
    }

    private companion object {
        const val SERVICE = "wiki.depasquale.currency.platform.functions.CurrencyAppFunctionsService"
        const val SERVICE_INTERFACE = "android.app.appfunctions.AppFunctionService"
        const val BIND_PERMISSION = "android.permission.BIND_APP_FUNCTION_SERVICE"
        const val V1_SCHEMA = "android.app.appfunctions"
        const val V2_SCHEMA = "android.app.appfunctions.v2"
        const val SCHEMA_DEFINITION = "android.app.appfunctions.schema"
        const val TEST_CONFIG = "com/android/tools/test_config.properties"
        const val MERGED_MANIFEST = "android_merged_manifest"
        const val ANDROID = "http://schemas.android.com/apk/res/android"
    }

}
