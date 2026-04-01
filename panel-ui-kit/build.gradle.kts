plugins {
    id("com.android.library")
    id("convention.compose")
    id("convention-publish")
    id("convention.detekt")
}

description = "Debug panel UI kit: theme, design tokens, shared components"

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

    namespace = "com.redmadrobot.debug.uikit"
}

kotlin {
    explicitApi()
}

dependencies {
    implementation(androidx.compose.material3)
    implementation(androidx.compose.ui.tooling)
    implementation(androidx.compose.ui.tooling.preview)
}
