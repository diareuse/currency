plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
}

dependencies {
    implementation(libs.android.gradle)
    implementation(libs.kotlin.gradle)
    implementation(libs.kotlin.serialization.gradle)
    implementation(libs.kotlin.compose.gradle)
    implementation(libs.ksp.gradle)
    implementation(libs.detekt.gradle)
    implementation(libs.google.services.gradle)
    implementation(libs.firebase.crashlytics.gradle)
}
