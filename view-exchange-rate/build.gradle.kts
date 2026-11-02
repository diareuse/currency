plugins {
    id("library-conventions")
    id("di-conventions")
}

android {

    namespace = "cursola.rate.view"

}

ksp {
    arg("spike.viewmodel.enabled", "false")
}

dependencies {

    api(project(":library-exchange-rate"))

    api(libs.lifecycle.viewmodel)
    api(libs.spike.androidx)

}
