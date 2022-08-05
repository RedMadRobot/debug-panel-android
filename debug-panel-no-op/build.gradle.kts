plugins {
    id(Plugins.Android.libraryPlagin)
    kotlin(Plugins.Kotlin.androidPlugin)
}

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
    }

    java.sourceSets.create("src.main.kotlin")
}

dependencies {
    implementation(Dependencies.kotlin.stdlib)
    implementation(Dependencies.okhttp)
    implementation(Dependencies.androidx.app_compat)
    implementation(Dependencies.feature_toggles.flipper)
    implementation(Dependencies.coroutines_android)
}
