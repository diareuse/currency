import com.google.devtools.ksp.gradle.KspAATask

plugins {
    id("com.google.devtools.ksp")
}

// KSP2 tasks running concurrently in one Gradle daemon (org.gradle.parallel) intermittently resolve
// boot-classpath types such as android.content.Context as <ERROR TYPE>, so Spike emits no entry point for
// that variant. Running every KspAATask (the only KSP2 task type) one at a time avoids the race.
val kspSerialService = gradle.sharedServices.registerIfAbsent("kspSerial", KspSerialService::class) {
    maxParallelUsages.set(1)
}

tasks.withType<KspAATask>().configureEach {
    usesService(kspSerialService)
}
