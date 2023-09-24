plugins {
    id(Plugins.Android.applicationPlugin)
    kotlin(Plugins.Kotlin.androidPlugin)
}

android {
    compileSdk = Project.COMPILE_SDK
    namespace = "com.redmadrobot.debugpanel"

    defaultConfig {
        minSdk = Project.MIN_SDK
        targetSdk = Project.TARGET_SDK
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    sourceSets.getByName("main") {
        java.srcDir("src/main/kotlin")
    }

    sourceSets.getByName("release") {
        java.srcDir("src/release/kotlin")
    }

    sourceSets.getByName("debug") {
        java.srcDir("src/debug/kotlin")
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = androidx.versions.compose.compiler.get()
    }
    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}


dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(androidx.appcompat)
    implementation(androidx.compose.runtime)
    implementation(stack.material)
    implementation(androidx.constraintlayout)
    implementation(rmr.flipper)
    implementation(stack.timber)
    implementation(stack.kotlinx.coroutines.android)
    implementation(androidx.lifecycle.runtime)
    debugImplementation(project(":core"))
    debugImplementation(project(":plugins:servers"))

    // Debug panel dependencies
    debugImplementation(project(":plugins:accounts"))
    debugImplementation(project(":plugins:app-settings"))
    debugImplementation(project(":plugins:flipper"))
    debugImplementation(project(":plugins:variable"))
    releaseImplementation(project(":no-op"))
//
//    debugImplementation("com.redmadrobot.debug:panel-core:0.7.5")
//    debugImplementation("com.redmadrobot.debug:accounts-plugin:0.7.5")
//    debugImplementation("com.redmadrobot.debug:servers-plugin:0.7.5")
//    debugImplementation("com.redmadrobot.debug:app-settings-plugin:0.7.5")
//    debugImplementation("com.redmadrobot.debug:flipper-plugin:0.7.5")
//    debugImplementation("com.redmadrobot.debug:variable-plugin:0.7.5")
//
    //No-op dependency
    //    releaseImplementation("com.redmadrobot.debug:panel-no-op:0.7.5")

    implementation(stack.retrofit)
}
