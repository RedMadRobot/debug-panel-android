plugins {
    id(Plugins.Android.libraryPlagin)
    kotlin(Plugins.Kotlin.androidPlugin)
    kotlin(Plugins.Kotlin.kapt)
    id("convention-publish")
}

description = "Plugin that helps to change the values of shared preferences"

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
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = androidx.versions.compose.compiler.get()
    }
    namespace = "com.redmadrobot.debug.app_settings"
}

kotlin {
    explicitApi()
}

dependencies {
    implementation(project(":core"))
    implementation(project(":common"))
    implementation(kotlin("stdlib"))
    kapt(androidx.room.compiler)
}
