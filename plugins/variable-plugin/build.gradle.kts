plugins {
    id(Plugins.Android.libraryPlagin)
    kotlin(Plugins.Kotlin.androidPlugin)
    kotlin(Plugins.Kotlin.androidExtensions)
    kotlin(Plugins.Kotlin.kapt)
    id("publishPlugin")
}

description = "Plugin allows you to change some variables without rebuilding/restarting the project"

android {
    compileSdk = Project.COMPILE_SDK

    defaultConfig {
        minSdk = Project.MIN_SDK
        targetSdk = Project.TARGET_SDK

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

    kotlin.sourceSets.main {
        kotlin.srcDirs("src/main/kotlin")
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = androidx.versions.compose.compiler.get()
    }
    namespace = "com.redmadrobot.variable_plugin"
}

dependencies {
    implementation(project(":debug-panel-core"))
    implementation(project(":debug-panel-common"))
    implementation(kotlin("stdlib-jdk8"))
}
