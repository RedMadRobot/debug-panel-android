plugins {
    id("com.android.application")
    kotlin("android")
    alias(stack.plugins.kotlin.compose)
}

android {
    compileSdk = Project.COMPILE_SDK
    namespace = "com.redmadrobot.debugpanel"

    defaultConfig {
        minSdk = Project.MIN_SDK
        targetSdk = Project.TARGET_SDK
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(androidx.appcompat)
    implementation(androidx.compose.runtime)
    implementation(stack.material)
    implementation(androidx.constraintlayout)
    implementation(rmr.flipper)
    implementation(rmr.konfeature)
    implementation(stack.timber)
    implementation(stack.kotlinx.coroutines.android)
    implementation(androidx.lifecycle.runtime)
    debugImplementation(project(":panel-core"))
    debugImplementation(project(":plugins:plugin-servers"))
    debugImplementation(project(":plugins:plugin-accounts"))
    debugImplementation(project(":plugins:plugin-app-settings"))

    // Debug panel dependencies
    debugImplementation(project(":plugins:plugin-flipper"))
    debugImplementation(project(":plugins:plugin-konfeature"))
    releaseImplementation(project(":panel-no-op"))

    implementation(stack.retrofit)
}
