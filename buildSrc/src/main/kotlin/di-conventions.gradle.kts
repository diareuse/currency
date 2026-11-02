plugins {
    id("ksp-conventions")
}

val libs = the<VersionCatalogsExtension>().named("libs")

dependencies {
    "implementation"(libs.findLibrary("spike").get())
    "ksp"(libs.findLibrary("spike-compiler").get())

    "testImplementation"(libs.findLibrary("spike-test").get())
    "kspTest"(libs.findLibrary("spike-compiler").get())
}
