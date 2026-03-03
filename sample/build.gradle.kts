plugins {
    id("com.android.application")
    kotlin("android")
    id("convention.compose")
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

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(androidx.appcompat)
    implementation(androidx.activity.compose)
    implementation(androidx.lifecycle.runtime)
    implementation(stack.kotlinx.coroutines.android)
    implementation(stack.material)
    implementation(stack.timber)
    implementation(rmr.konfeature)

    debugImplementation(project(":panel-core"))
    debugImplementation(project(":plugins:plugin-servers"))
    debugImplementation(project(":plugins:plugin-about-app"))

    // Debug panel dependencies
    debugImplementation(project(":plugins:plugin-konfeature"))
    releaseImplementation(project(":panel-no-op"))

    implementation(stack.retrofit)
}
