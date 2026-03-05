plugins {
    id("com.android.library")
    id("convention-publish")
    id("convention.detekt")
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

dependencies {
    implementation(stack.kotlin.stdlib)
    implementation(androidx.appcompat)
    implementation(stack.okhttp)
    implementation(stack.kotlinx.coroutines.android)
    implementation(rmr.konfeature)
}
