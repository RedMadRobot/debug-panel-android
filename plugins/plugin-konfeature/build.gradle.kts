plugins {
    id(Plugins.Android.libraryPlagin)
    kotlin(Plugins.Kotlin.androidPlugin)
    kotlin(Plugins.Kotlin.kapt)
    alias(stack.plugins.kotlin.compose)
    id("convention-publish")
}

description = "Plugin for konfeature library integration"

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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
        freeCompilerArgs += "-Xexplicit-api=strict"
    }

    namespace = "com.redmadrobot.debug.plugin.konfeature"
}

dependencies {
    implementation(project(":panel-core"))
    implementation(project(":panel-common"))
    implementation(androidx.lifecycle.runtime)
}
