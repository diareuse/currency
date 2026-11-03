plugins {
    id("library-conventions")
    id("di-conventions")
    id("org.jetbrains.kotlin.plugin.serialization")
}

val schemasDir = "$projectDir/schemas"

android {

    namespace = "cursola.rate"

    sourceSets {
        getByName("androidTest").assets.srcDirs(files(schemasDir))
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
        unitTests.all { it.jvmArgs("--add-opens=java.base/jdk.internal.access=ALL-UNNAMED") }
    }

}

ksp {
    arg("room.schemaLocation", schemasDir)
}


dependencies {

    implementation(libs.core.ktx)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)

    implementation(libs.room.runtime)
    implementation(libs.room.core)
    ksp(libs.room.compiler)

    testImplementation(libs.kxml2)
    testImplementation(libs.icu4j)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.test.ext.junit)

}
