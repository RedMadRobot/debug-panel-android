plugins {
    id(Plugins.Android.libraryPlagin)
    kotlin(Plugins.Kotlin.androidPlugin)
    kotlin(Plugins.Kotlin.kapt)
    id("publishPlugin")
}

description = "Debug panel core library"

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
        freeCompilerArgs += listOf("-opt-in=kotlin.RequiresOptIn")
    }

    buildFeatures {
        viewBinding = true
    }
    namespace = "com.redmadrobot.panel_core"
}

kotlin {
    explicitApi()
}

dependencies {
    implementation(project(":debug-panel-common"))
    kapt(Dependencies.room.compiler)
}
