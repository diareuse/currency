import java.util.Calendar

plugins {
    id("com.android.application")
    id("common-conventions")
    id("signing-conventions")
}

val calendar: Calendar = Calendar.getInstance()
val year = calendar.get(Calendar.YEAR)
val month = calendar.get(Calendar.MONTH) + 1
val dateVersion = "$year.$month"

android {

    namespace = "wiki.depasquale.currency"

    defaultConfig {
        applicationId = "wiki.depasquale.cursola"
        versionCode = providers.gradleProperty("versionCode").map(String::toInt).getOrElse(1)
        versionName = dateVersion
    }

    buildTypes {

        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

    }

}
