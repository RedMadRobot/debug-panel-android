plugins {
    id("com.android.library")
    kotlin("android")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    namespace = "com.redmadrobot.debug.noop"
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(stack.okhttp)
    implementation(androidx.appcompat)
    implementation(rmr.konfeature)
    implementation(stack.kotlinx.coroutines.android)
}
