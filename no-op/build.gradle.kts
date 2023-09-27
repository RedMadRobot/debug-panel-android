plugins {
    id(Plugins.Android.libraryPlagin)
    kotlin(Plugins.Kotlin.androidPlugin)
    id("publishPlugin")
}

description = "Debug panel no-op dependency module"

android {
    compileSdk = Project.COMPILE_SDK

    defaultConfig {
        minSdk = Project.MIN_SDK
        targetSdk = Project.TARGET_SDK

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

    java.sourceSets.create("src.main.kotlin")
    namespace = "com.redmadrobot.debug.no_op"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(stack.okhttp)
    implementation(androidx.appcompat)
    implementation(rmr.flipper)
    implementation(stack.kotlinx.coroutines.android)
}
