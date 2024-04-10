// Top-level build file where you can add configuration options common to all sub-projects/modules.
subprojects {
    version = getLibVersion()
    group = "com.redmadrobot.debug"

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions.freeCompilerArgs += listOf(
            "-opt-in=com.redmadrobot.debug_panel_core.annotation.DebugPanelInternal"
        )
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}
