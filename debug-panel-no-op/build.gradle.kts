plugins {
    id(Plugins.Android.libraryPlagin)
    kotlin(Plugins.Kotlin.androidPlugin)
    id("convention-publish")
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    namespace = "com.redmadrobot.debug_panel_no_op"
}

dependencies {
    implementation(Dependencies.kotlin.stdlib)
    implementation(Dependencies.okhttp)
    implementation(Dependencies.androidx.app_compat)
    implementation(Dependencies.feature_toggles.flipper)
    implementation(Dependencies.coroutines_android)
}
