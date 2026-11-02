import com.android.build.api.dsl.ApplicationExtension
import java.util.Properties

val keystorePropertiesFile: File = rootProject.file("keystore.properties")

if (keystorePropertiesFile.exists()) {
    val keystoreProperties = Properties().apply {
        keystorePropertiesFile.inputStream().use(::load)
    }

    extensions.configure<ApplicationExtension>("android") {

        signingConfigs {

            val root = maybeCreate("root").apply {
                storeFile = rootProject.file("keystore.jks")
                storePassword = keystoreProperties["store.password"] as String?
            }

            maybeCreate("debug").apply {
                initWith(root)
                keyAlias = keystoreProperties["key.debug.name"] as String?
                keyPassword = keystoreProperties["key.debug.password"] as String?
            }

            maybeCreate("release").apply {
                initWith(root)
                keyAlias = keystoreProperties["key.release.name"] as String?
                keyPassword = keystoreProperties["key.release.password"] as String?
            }

        }

        buildTypes {

            getByName("release") {
                signingConfig = signingConfigs.getByName("release")
            }

            getByName("debug") {
                signingConfig = signingConfigs.getByName("debug")
            }

        }

    }
}
