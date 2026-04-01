import internal.Versions
import internal.android

plugins {
    id("com.android.library")
    id("convention-publish")
    id("convention.compose")
    id("convention.detekt")
}

android {
    compileSdk = Versions.COMPILE_SDK
    lint.targetSdk = Versions.TARGET_SDK

    defaultConfig {
        minSdk = Versions.MIN_SDK
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

kotlin {
    explicitApi()
    jvmToolchain(17)
}

dependencies {
    implementation(project(":panel-core"))
    implementation(project(":panel-ui-kit"))
}
