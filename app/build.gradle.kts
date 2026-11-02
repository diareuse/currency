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

    testOptions {
        unitTests.isIncludeAndroidResources = true
        unitTests.all {
            it.jvmArgs("--add-opens=java.base/jdk.internal.access=ALL-UNNAMED")
            it.forkEvery = 1
        }
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

ksp {
    arg("spike.viewmodel.enabled", "false")
}

dependencies {

    implementation(project(":app-view"))
    implementation(project(":view-exchange-rate"))

    implementation(libs.activity.compose)
    implementation(libs.appcompat)
    implementation(libs.navigation.compose)
    implementation(libs.startup.runtime)
    implementation(libs.work.runtime)
    implementation(libs.spike.androidx.compose)
    implementation(libs.material)
    implementation(libs.slice.builders)
    implementation(libs.slice.core)
    implementation(libs.slice.view)

    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.test.ext.junit)
    testImplementation(libs.work.testing)
    testImplementation(libs.compose.ui.test.junit4)

    releaseImplementation(platform(libs.firebase.bom))
    releaseImplementation(libs.firebase.analytics)
    releaseImplementation(libs.firebase.crashlytics)
    releaseImplementation(libs.firebase.perf)

}
