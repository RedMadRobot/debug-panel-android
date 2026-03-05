plugins {
    id("com.android.library")
    id("convention.compose")
    id("convention-publish")
    id("convention.detekt")
}

description = "Debug panel core library"

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
    }

    lint {
        disable += "CoroutineCreationDuringComposition"
    }

    namespace = "com.redmadrobot.debug.core"
}

kotlin {
    explicitApi()
}

dependencies {
    implementation(androidx.activity.compose)
    implementation(androidx.navigation.compose)
    implementation(androidx.core)
    implementation(androidx.appcompat)
    implementation(androidx.lifecycle.livedata)

    implementation(stack.kotlinx.coroutines.android)
    implementation(stack.timber)

    api(androidx.lifecycle.viewmodel)
}
