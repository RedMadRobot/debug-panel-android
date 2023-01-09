// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "7.2.0" apply false
    id("com.android.library") version "7.2.0" apply false
    kotlin("android") version "1.7.21" apply false

}
subprojects {

    version = getLibVersion()
    group = "com.redmadrobot.debug"

    repositories {
        google()
        jcenter()
        mavenLocal()
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions.freeCompilerArgs += listOf(
            "-Xopt-in=com.redmadrobot.debug.core.annotation.DebugPanelInternal"
        )
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
