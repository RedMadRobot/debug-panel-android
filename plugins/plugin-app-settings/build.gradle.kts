plugins {
    id(Plugins.Android.libraryPlagin)
    kotlin(Plugins.Kotlin.androidPlugin)
    alias(stack.plugins.kotlin.compose)
    id("convention-publish")
}

description = "Plugin that helps to change the values of shared preferences"

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

    namespace = "com.redmadrobot.debug.plugin.appsettings"
}

kotlin {
    explicitApi()
}

dependencies {
    implementation(project(":panel-core"))
    implementation(project(":panel-common"))
    implementation(kotlin("stdlib"))
}
