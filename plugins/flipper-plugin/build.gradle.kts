plugins {
    id(Plugins.Android.libraryPlagin)
    kotlin(Plugins.Kotlin.androidPlugin)
    kotlin(Plugins.Kotlin.kapt)
    id("publishPlugin")
}

description = "Plugin for flipper library integration"

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs += "-Xexplicit-api=strict"
    }

    buildFeatures {
        viewBinding = true
    }
    namespace = "com.redmadrobot.flipper_plugin"
}

dependencies {
    implementation(project(":debug-panel-core"))
    implementation(project(":debug-panel-common"))
    implementation(Dependencies.lifecycle.lifecycle_runtime)
}
