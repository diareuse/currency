plugins {
    id("application-conventions")
    id("di-conventions")
    id("google-conventions")
    id("compose-conventions")
}

android {

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    packaging {
        resources {
            pickFirsts += listOf(
                "META-INF/AL2.0",
                "META-INF/LGPL2.1",
                "win32-x86-64/attach_hotspot_windows.dll",
                "win32-x86/attach_hotspot_windows.dll",
                "META-INF/licenses/ASM",
            )
        }
    }

}

dependencies {

    implementation(project(":app-view"))
    implementation(project(":view-exchange-rate"))

    implementation(libs.activity.compose)
    implementation(libs.appcompat)
    implementation(libs.navigation.compose)
    implementation(libs.startup.runtime)
    implementation(libs.work.runtime)
    implementation(libs.material)
    implementation(libs.slice.builders)
    implementation(libs.slice.core)
    implementation(libs.slice.view)

    releaseImplementation(platform(libs.firebase.bom))
    releaseImplementation(libs.firebase.analytics)
    releaseImplementation(libs.firebase.crashlytics)
    releaseImplementation(libs.firebase.perf)

}
