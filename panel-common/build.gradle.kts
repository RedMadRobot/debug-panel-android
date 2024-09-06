plugins {
    id(Plugins.Android.libraryPlagin)
    kotlin(Plugins.Kotlin.androidPlugin)
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
        freeCompilerArgs += "-Xexplicit-api=strict"
    }


    buildFeatures {
        viewBinding = true
    }
    namespace = "com.redmadrobot.debug.panel.common"
}

dependencies {
    api(androidx.activity.compose)
    api(androidx.compose.animation)
    api(androidx.compose.foundation)
    api(androidx.compose.material)
    api(androidx.compose.runtime.livedata)
    api(androidx.compose.ui)
    api(androidx.compose.ui.tooling)
    api(androidx.compose.ui.tooling.preview)
    api(androidx.compose.ui.viewbinding)
    api(androidx.constraintlayout.compose)
    api(androidx.navigation.compose)
    api(androidx.core)
    api(androidx.fragment)
    api(androidx.lifecycle.viewmodel.compose)
    api(androidx.room)
    api(androidx.room.runtime)
    api(rmr.flipper)
    api(rmr.itemsadapter.viewbinding)
    api(rmr.konfeature)
    api(stack.accompanist.themeadapter.core)
    api(stack.accompanist.themeadapter.material)
    api(stack.kotlinx.coroutines.android)
    api(stack.okhttp)
    api(stack.timber)
    // legacy
    api(androidx.appcompat)
    api(androidx.constraintlayout)
    api(androidx.lifecycle.livedata)
    api(androidx.lifecycle.livedata.core)
    api(androidx.lifecycle.runtime)
    api(androidx.lifecycle.viewmodel)
    api(stack.material)
}

tasks.register("prepareKotlinBuildScriptModel") {}
