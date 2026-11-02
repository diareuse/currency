import dagger.hilt.android.plugin.HiltExtension

plugins {
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

val libs = the<VersionCatalogsExtension>().named("libs")

extensions.configure<HiltExtension> {
    enableAggregatingTask = true
}

dependencies {
    "implementation"(libs.findLibrary("hilt-android").get())
    "implementation"(libs.findLibrary("hilt-navigation-compose").get())
    "implementation"(libs.findLibrary("hilt-work").get())
    "ksp"(libs.findLibrary("hilt-compiler").get())

    "androidTestImplementation"(libs.findLibrary("hilt-android-testing").get())
    "kspAndroidTest"(libs.findLibrary("hilt-compiler").get())

    "testImplementation"(libs.findLibrary("hilt-android-testing").get())
    "kspTest"(libs.findLibrary("hilt-compiler").get())
}
