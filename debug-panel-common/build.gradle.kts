plugins {
    id(Plugins.Android.libraryPlagin)
    kotlin(Plugins.Kotlin.androidPlugin)
    kotlin(Plugins.Kotlin.kapt)
    id("convention-publish")
}

description = "Debug panel common components"

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
        freeCompilerArgs += "-Xexplicit-api=strict"
    }

    buildFeatures {
        viewBinding = true
    }
    namespace = "com.redmadrobot.debug_panel_common"
}

dependencies {
    api(kotlin("stdlib"))
    api(stack.okhttp)
    api(androidx.appcompat)
    api(stack.material)
    api(androidx.constraintlayout)
    api(stack.kotlinx.coroutines.android)
    api(androidx.lifecycle.runtime)
    api(androidx.lifecycle.livedata)
    api(androidx.lifecycle.livedata.core)
    api(androidx.lifecycle.viewmodel)
    api(androidx.room.runtime)
    api(androidx.room)
    api(androidx.core)
    api(rmr.itemsadapter.viewbinding)
    api(rmr.flipper)
    api(stack.timber)
    kapt(androidx.room.compiler)
}

tasks.register("prepareKotlinBuildScriptModel") {}
