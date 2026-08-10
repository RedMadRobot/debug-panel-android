import internal.CheckNoopApiTask

plugins {
    id("com.android.library")
    id("convention-publish")
    id("convention.detekt")
    id("convention.abi.validation")
}

description = "Debug panel no-op dependency module"

android {
    compileSdk = Project.COMPILE_SDK
    lint.targetSdk = Project.TARGET_SDK

    defaultConfig {
        minSdk = Project.MIN_SDK

        consumerProguardFile("consumer-rules.pro")
    }

    buildTypes {
        getByName(Project.BuildTypes.release) {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile(Project.Proguard.androidOptimizedRules),
                Project.Proguard.projectRules
            )
        }
    }

    kotlin {
        jvmToolchain(17)
        explicitApi()
    }

    namespace = "com.redmadrobot.debug.noop"
}

// Modules whose public API this module replaces in release builds: every published module except
// the panel's own UI kit, which consumers do not depend on directly.
val notMirrored = setOf(project.name, "panel-ui-kit", "sample")
val mirroredModules = rootProject.subprojects
    .filter { it.subprojects.isEmpty() && it.name !in notMirrored }

val checkNoopApi = tasks.register<CheckNoopApiTask>("checkNoopApi") {
    description = "Checks that ${project.name} covers the public API of the modules it replaces."
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    noopDump.set(layout.projectDirectory.file("api/${project.name}.api"))
    mirroredDumps.from(mirroredModules.map { it.layout.projectDirectory.file("api/${it.name}.api") })
}

tasks.named(LifecycleBasePlugin.CHECK_TASK_NAME) {
    dependsOn(checkNoopApi)
}

dependencies {
    implementation(stack.kotlin.stdlib)
    implementation(androidx.appcompat)
    implementation(stack.okhttp)
    implementation(stack.kotlinx.coroutines.android)
    implementation(rmr.konfeature)
    implementation(rmr.konfeature.ui.noop)
}
