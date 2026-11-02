import com.android.build.api.dsl.CommonExtension

plugins {
    id("org.jetbrains.kotlin.plugin.compose")
}

val libs = the<VersionCatalogsExtension>().named("libs")

extensions.configure<CommonExtension>("android") {

    buildFeatures.compose = true

}

dependencies {
    "implementation"(platform(libs.findLibrary("compose-bom").get()))
}
