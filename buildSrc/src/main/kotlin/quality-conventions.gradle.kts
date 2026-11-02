plugins {
    id("io.gitlab.arturbosch.detekt")
}

detekt {
    config.setFrom(files("${rootProject.projectDir}/config/config.yml"))
    baseline = file("${rootProject.projectDir}/config/baseline.xml")

    reports {
        txt.required.set(false)
        html.required.set(false)
        xml.required.set(false)
        sarif.required.set(true)
    }
}
