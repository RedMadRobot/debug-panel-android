plugins {
    id(Plugins.Android.libraryPlagin)
    kotlin(Plugins.Kotlin.androidPlugin)
    alias(stack.plugins.ksp)
    alias(stack.plugins.kotlin.compose)
    id("convention-publish")
}

description = "Plugin for switching user accounts"

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

    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }

    namespace = "com.redmadrobot.debug.plugin.accounts"
}

kotlin {
    explicitApi()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":panel-core"))
    implementation(project(":panel-common"))
    ksp(androidx.room.compiler)
}
