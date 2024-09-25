// Top-level build file where you can add configuration options common to all sub-projects/modules.

subprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        compilerOptions {
            freeCompilerArgs.add("-opt-in=com.redmadrobot.debug.core.annotation.DebugPanelInternal")
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}
