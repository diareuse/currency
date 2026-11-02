import com.google.devtools.ksp.gradle.KspAATask

plugins {
    id("com.google.devtools.ksp")
}

val kspSerialService = gradle.sharedServices.registerIfAbsent("kspSerial", KspSerialService::class) {
    maxParallelUsages.set(1)
}

tasks.withType<KspAATask>().configureEach {
    usesService(kspSerialService)
}
