import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension

plugins {
    id("quality-conventions")
}

val libs = the<VersionCatalogsExtension>().named("libs")
val targetSdkVersion = 37

extensions.configure<CommonExtension>("android") {
    compileSdk = 37
    defaultConfig.apply {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions.apply {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildTypes.apply {
        getByName("debug") {
            enableUnitTestCoverage = true
        }
    }
}

plugins.withId("com.android.application") {
    extensions.configure<ApplicationExtension> {
        defaultConfig.targetSdk = targetSdkVersion
    }
}

// Library defaultConfig no longer exposes targetSdk in AGP 9; these are its public replacements.
plugins.withId("com.android.library") {
    extensions.configure<LibraryExtension> {
        testOptions.targetSdk = targetSdkVersion
        lint.targetSdk = targetSdkVersion
    }
}

dependencies {

    "implementation"(libs.findLibrary("coroutines-core").get())

    "testImplementation"(libs.findLibrary("kotlin-test").get())
    "testImplementation"(libs.findLibrary("kotlin-test-junit").get())
    "testImplementation"(libs.findLibrary("coroutines-test").get())
    "testImplementation"(libs.findLibrary("mockito-core").get())
    "testImplementation"(libs.findLibrary("mockito-kotlin").get())

}
