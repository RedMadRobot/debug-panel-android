// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath(Plugins.Android.gradlePlugin)
        classpath(Plugins.Kotlin.plugin)
    }
}

subprojects {
    repositories {
        google()
        jcenter()
        mavenLocal()
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions.freeCompilerArgs += listOf(
            "-Xopt-in=com.redmadrobot.debug_panel_core.annotation.DebugPanelInternal"
        )
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
